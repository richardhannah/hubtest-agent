package com.richard.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.richard.Application;
import com.richard.models.batchprocess.BatchResponse;
import com.richard.models.batchprocess.BatchTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by richard on 27/07/2016.
 */
public class BatchResponder {

    private static Logger LOGGER = LogManager.getLogger(BatchResponder.class);

    @Autowired
    RestTemplate inthubRestTemplate;

    @Autowired
    NamedParameterJdbcTemplate jdbcInternalTemplate;

    public void ProduceBatchResponse(List<BatchTransaction> batchTransactions){

    Thread thread = new Thread(new BatchResponseRunnable(batchTransactions,inthubRestTemplate,jdbcInternalTemplate));
        thread.start();

    }




}
