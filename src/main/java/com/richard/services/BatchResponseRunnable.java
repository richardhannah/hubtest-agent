package com.richard.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.richard.models.batchprocess.BatchResponse;
import com.richard.models.batchprocess.BatchTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.richard.utils.Thread;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by richard on 27/07/2016.
 */

@Component
public class BatchResponseRunnable implements Runnable {


    private static Logger LOGGER = LogManager.getLogger(BatchResponseRunnable.class);

    RestTemplate inthubRestTemplate;
    NamedParameterJdbcTemplate jdbcInternalTemplate;
    List<BatchTransaction> batchTransactions;
    ObjectMapper mapper;
    List<BatchResponse> batchResponses = new ArrayList<>();
    List<Map<String,String>> termsAcceptList = new ArrayList<>();
    List<Map<String,String>> regList = new ArrayList<>();

    public BatchResponseRunnable(){}

    public BatchResponseRunnable(List<BatchTransaction> batchTransactions,RestTemplate inthubRestTemplate, NamedParameterJdbcTemplate jdbcInternalTemplate){
        this.batchTransactions = batchTransactions;
        this.inthubRestTemplate=inthubRestTemplate;
        this.jdbcInternalTemplate=jdbcInternalTemplate;
    }

    @Override
    public void run() {
        LOGGER.trace("starting thread");
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        persistBatch();
        Thread.wait(5000);
        generateBatchResponse();
        sendBatchResponse();
        Thread.wait(20000);
        sendRegistration();
        Thread.wait(5000);
        sendAcceptTerms();
        Thread.wait(20000);


    }




    private String NSIDBuilder(String partnerId){


        int accountId = jdbcInternalTemplate.queryForObject("SELECT id FROM hubtest_agent.account order by id desc limit 1;",new MapSqlParameterSource(), Integer.class);
        int vehicleId = jdbcInternalTemplate.queryForObject("SELECT id FROM hubtest_agent.account order by id desc limit 1;",new MapSqlParameterSource(), Integer.class);


        return accountId+":"+partnerId+":"+vehicleId;
    }

    private String nsidFromSharedAcctId(String sharedAcctId){

        String[] splitString = sharedAcctId.split(":");
        return splitString[0];

    }



    private String serialize(Object obj) {

        String response = "";
        try {
            response = mapper.writeValueAsString(obj);
        }
        catch (Exception ex){}

        return response;
    }


    private void generateBatchResponse(){
        for (BatchTransaction transaction : batchTransactions ) {

            BatchResponse response = new BatchResponse();
            response.setGPID(transaction.getGPID());
            response.setIHID(transaction.getVehicles().get(0).getIHID());
            response.setNSID(NSIDBuilder(transaction.getPartner()));
            response.setBTID(Integer.parseInt(transaction.getBTID()));
            response.setErrorCode(0);
            response.setPartner_id(transaction.getPartner());

            batchResponses.add(response);


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

            Map<String,String> acceptTerms = new HashMap<>();
            acceptTerms.put("accepted_tcs", LocalDateTime.now().format(formatter).toString() + " BST");
            acceptTerms.put("customer_id", nsidFromSharedAcctId(response.getNSID()));
            termsAcceptList.add(acceptTerms);

            Map<String,String> registration = new HashMap<>();
            registration.put("first_registered",LocalDateTime.now().format(formatter).toString() + " BST");
            registration.put("customer_id", nsidFromSharedAcctId(response.getNSID()));
            regList.add(registration);

        }

    }

    private void sendBatchResponse(){
        String json = "";
        LOGGER.trace("Sending response: " + json);
        try {
            json = mapper.writeValueAsString(batchResponses);
        } catch (Exception ex) {
            LOGGER.error("Failed to Serialize object");
        }
        String resp = inthubRestTemplate.postForObject("http://localhost:8080/Integration-Hub/br/upload", batchResponses,String.class);
    }

    private void sendRegistration(){

        for(Map<String,String> registration : regList) {
            LOGGER.trace("sending registration : " + serialize(registration));
            ResponseEntity<String> resp = inthubRestTemplate.postForEntity("http://localhost:8080/Integration-Hub/mda/user_registration/sandbox", registration, String.class);
            LOGGER.trace("registration response : " + resp.getBody());
        }
    }

    private void sendAcceptTerms(){

        for(Map<String,String> termsAccept : termsAcceptList) {
            LOGGER.trace("sending accept terms : " + serialize(termsAccept));
            ResponseEntity<String> resp = inthubRestTemplate.postForEntity("http://localhost:8080/Integration-Hub/mda/terms_acceptance/sandbox", termsAccept, String.class);
            LOGGER.trace("accept terms response : " + resp.getBody());
        }
    }

    private void persistBatch(){
        String json = "";
        try {
            json = mapper.writeValueAsString(batchTransactions);
        } catch (Exception ex) {
            LOGGER.error("Failed to Deserialize object");
        }

        jdbcInternalTemplate.update("INSERT into hubtest_agent.account (raw) values (:json)", new MapSqlParameterSource().addValue("json", json));
        jdbcInternalTemplate.update("INSERT into hubtest_agent.vehicle (raw) values (:json)", new MapSqlParameterSource().addValue("json", json));
    }





}
