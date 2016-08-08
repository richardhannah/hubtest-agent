package com.richard.services;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.richard.models.batchprocess.BatchTransaction;
import com.richard.models.batchprocess.OFItem;
import com.richard.models.batchprocess.OFVehicle;
import com.richard.models.batchprocess.OrderFulfillment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.client.RestTemplate;
import com.richard.utils.Thread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by richard on 08/08/2016.
 */
public class FulfillOrderRunnable implements Runnable {

    private static Logger LOGGER = LogManager.getLogger(FulfillOrderRunnable.class);

    RestTemplate inthubRestTemplate;
    NamedParameterJdbcTemplate jdbcInternalTemplate;
    ObjectMapper mapper;

    private int customerId;

    public FulfillOrderRunnable() {
    }

    public FulfillOrderRunnable(int customerId,RestTemplate inthubRestTemplate, NamedParameterJdbcTemplate jdbcInternalTemplate) {
        this.customerId = customerId;
        this.inthubRestTemplate = inthubRestTemplate;
        this.jdbcInternalTemplate = jdbcInternalTemplate;
    }

    @Override
    public void run() {

        LOGGER.trace("fulfill order thread started");

        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Thread.wait(5000);
        OrderFulfillment orderFulfillment = new OrderFulfillment(fetchBatchTransaction(customerId));

        try {
            LOGGER.trace(mapper.writeValueAsString(orderFulfillment));
        }
        catch (Exception ex){
            LOGGER.error("Json processing exception",ex);
        }


    }

    private BatchTransaction fetchBatchTransaction(int customer_id) {

        String result = jdbcInternalTemplate.queryForObject("SELECT raw FROM hubtest_agent.account WHERE id=:id",new MapSqlParameterSource().addValue("id", customer_id),String.class);
        LOGGER.trace(result);
        BatchTransaction batchTransaction =null;
        try {
            List<BatchTransaction> batchTransactions = mapper.readValue(result, new TypeReference<List<BatchTransaction>>(){});
            batchTransaction = batchTransactions.get(0);
        }
        catch (Exception ex){
            LOGGER.error("failed to map batchtransaction",ex);
        }
        return batchTransaction;

    }


}
