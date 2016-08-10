package com.richard.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.richard.dal.HubtestAgentDao;
import com.richard.models.batchprocess.BatchResponse;
import com.richard.models.batchprocess.BatchTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.richard.utils.Tools;
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
    HubtestAgentDao hubtestAgentDao;
    List<BatchTransaction> batchTransactions;
    ObjectMapper mapper;
    List<BatchResponse> batchResponses = new ArrayList<>();
    List<Map<String,String>> termsAcceptList = new ArrayList<>();
    List<Map<String,String>> regList = new ArrayList<>();

    public BatchResponseRunnable(){}

    public BatchResponseRunnable(List<BatchTransaction> batchTransactions,RestTemplate inthubRestTemplate, HubtestAgentDao hubtestAgentDao){
        this.batchTransactions = batchTransactions;
        this.inthubRestTemplate=inthubRestTemplate;
        this.hubtestAgentDao=hubtestAgentDao;
    }

    @Override
    public void run() {
        LOGGER.trace("starting thread");
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        persistBatch();
        createCustomersAndVehicles(batchTransactions);
        Tools.wait(5000);
        generateBatchResponse();
        sendBatchResponse();
        Tools.wait(20000);
        sendRegistration();
        Tools.wait(5000);
        sendAcceptTerms();
    }




    private String NSIDBuilder(BatchTransaction batchTransaction,BatchTransaction.Vehicle vehicle){
        int accountId = hubtestAgentDao.fetchCustomerByAcctNo(Integer.valueOf(batchTransaction.getCustomer().getAccount_no()));
        int vehicleId = hubtestAgentDao.fetchVehicleIdByIhid(vehicle.getIHID());
        return accountId+":"+batchTransaction.getPartner()+":"+vehicleId;
    }

    private String nsidFromSharedAcctId(String sharedAcctId){

        String[] splitString = sharedAcctId.split(":");
        return splitString[0];

    }

    private void generateBatchResponse(){
        for (BatchTransaction transaction : batchTransactions ) {

            for(BatchTransaction.Vehicle vehicle : transaction.getVehicles()){
                BatchResponse response = new BatchResponse();
                response.setGPID(transaction.getGPID());
                response.setIHID(vehicle.getIHID());
                response.setNSID(NSIDBuilder(transaction,vehicle));
                response.setBTID(Integer.parseInt(transaction.getBTID()));
                response.setErrorCode(0);
                response.setPartner_id(transaction.getPartner());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

                Map<String,String> acceptTerms = new HashMap<>();
                acceptTerms.put("accepted_tcs", LocalDateTime.now().format(formatter).toString() + " BST");
                acceptTerms.put("customer_id", nsidFromSharedAcctId(response.getNSID()));
                termsAcceptList.add(acceptTerms);

                Map<String,String> registration = new HashMap<>();
                registration.put("first_registered",LocalDateTime.now().format(formatter).toString() + " BST");
                registration.put("customer_id", nsidFromSharedAcctId(response.getNSID()));
                regList.add(registration);

                batchResponses.add(response);

            }

        }

    }

    private void sendBatchResponse(){
        LOGGER.debug("sending response : " + Tools.serialize(batchResponses));
        String resp = inthubRestTemplate.postForObject("http://localhost:8080/Integration-Hub/br/upload", Tools.serialize(batchResponses),String.class);
    }

    private void sendRegistration(){

        for(Map<String,String> registration : regList) {
            LOGGER.debug("sending registration : " + Tools.serialize(registration));
            ResponseEntity<String> resp = inthubRestTemplate.postForEntity("http://localhost:8080/Integration-Hub/mda/user_registration/sandbox", registration, String.class);
            LOGGER.trace("registration response : " + resp.getBody());
        }
    }

    private void sendAcceptTerms(){

        for(Map<String,String> termsAccept : termsAcceptList) {
            LOGGER.debug("sending accept terms : " + Tools.serialize(termsAccept));
            ResponseEntity<String> resp = inthubRestTemplate.postForEntity("http://localhost:8080/Integration-Hub/mda/terms_acceptance/sandbox", termsAccept, String.class);
            LOGGER.trace("accept terms response : " + resp.getBody());
        }
    }

    private void createCustomersAndVehicles(List<BatchTransaction> batchTransactions){

        for(BatchTransaction batchTransaction : batchTransactions){
            int customerId = hubtestAgentDao.createCustomer(batchTransaction);
            LOGGER.trace("netsuite customer id generated : " + customerId);
            for(BatchTransaction.Vehicle vehicle : batchTransaction.getVehicles()){
                hubtestAgentDao.createVehicle(customerId,vehicle);
            }
        }
    }

    private void persistBatch(){

        for(BatchTransaction batchTransaction : batchTransactions){
            hubtestAgentDao.saveTransaction(batchTransaction);
        }
    }
}
