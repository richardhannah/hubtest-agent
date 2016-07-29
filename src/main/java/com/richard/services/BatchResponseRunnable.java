package com.richard.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.richard.models.batchprocess.BatchResponse;
import com.richard.models.batchprocess.BatchTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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

    public BatchResponseRunnable(){}

    public BatchResponseRunnable(List<BatchTransaction> batchTransactions,RestTemplate inthubRestTemplate, NamedParameterJdbcTemplate jdbcInternalTemplate){
        this.batchTransactions = batchTransactions;
        this.inthubRestTemplate=inthubRestTemplate;
        this.jdbcInternalTemplate=jdbcInternalTemplate;
    }

    @Override
    public void run() {
        LOGGER.trace("starting thread");

        try {
            Thread.sleep(5000);
        } catch (Exception ex) {
            LOGGER.error("Thread sleep failed");
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String json = "";
        try {
            json = mapper.writeValueAsString(batchTransactions);
        } catch (Exception ex) {
            LOGGER.error("Failed to Deserialize object");
        }

        jdbcInternalTemplate.update("INSERT into hubtest_agent.account (raw) values (:json)", new MapSqlParameterSource().addValue("json", json));
        jdbcInternalTemplate.update("INSERT into hubtest_agent.vehicle (raw) values (:json)", new MapSqlParameterSource().addValue("json", json));

        List<BatchResponse> batchResponses = new ArrayList<>();

        for (BatchTransaction transaction : batchTransactions ) {

            BatchResponse response = new BatchResponse();
            response.setGPID(transaction.getGPID());
            response.setIHID(transaction.getVehicles().get(0).getIHID());
            response.setNSID(NSIDBuilder(transaction.getPartner()));
            response.setBTID(Integer.parseInt(transaction.getBTID()));
            response.setErrorCode(0);
            response.setPartner_id(transaction.getPartner());

            try {
                json = mapper.writeValueAsString(response);
            } catch (Exception ex) {
                LOGGER.error("Failed to Serialize object");
            }
            LOGGER.trace("Sending response: " + json);
            batchResponses.add(response);
        }
        inthubRestTemplate.postForObject("http://localhost:8080/Integration-Hub/br/upload", batchResponses, List.class);

    }

    private String NSIDBuilder(String partnerId){


        int accountId = jdbcInternalTemplate.queryForObject("SELECT id FROM hubtest_agent.account order by id desc limit 1;",new MapSqlParameterSource(), Integer.class);
        int vehicleId = jdbcInternalTemplate.queryForObject("SELECT id FROM hubtest_agent.account order by id desc limit 1;",new MapSqlParameterSource(), Integer.class);


        return accountId+":"+partnerId+":"+vehicleId;
    }



}
