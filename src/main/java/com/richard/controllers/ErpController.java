package com.richard.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.richard.models.batchprocess.BatchResponse;
import com.richard.models.batchprocess.BatchTransaction;
import com.richard.services.BatchResponder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by richard on 21/07/2016.
 */
@RestController
@RequestMapping("/erp")
public class ErpController {

//@RequestMapping(value= "accounts", method=RequestMethod.POST, consumes={com.himexubi.himexubiobjects.domain.model.v1.authentication.Account.MEDIA_TYPE})

    private static Logger LOGGER = LogManager.getLogger(ErpController.class);

    @Autowired
    BatchResponder batchResponder;


    @RequestMapping("/hello")
    public String index() {
        return "Greetings from Spring Boot!";
    }



    @RequestMapping(value = "/netsuite", method = RequestMethod.POST)
    public ResponseEntity<Void> netsuite (@RequestBody Object requestBody, @RequestParam int script){
        LOGGER.trace("Script id: " + script);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String json = "";
        try {
            LOGGER.trace("Request Recieved : "+objectMapper.writeValueAsString(requestBody));
        } catch (Exception ex) {
            LOGGER.error("Failed to Deserialize object");
        }

        switch (script){
            case 81:
                List<BatchTransaction> batchTransactions = new ArrayList<>();
                try {
                    json = objectMapper.writeValueAsString(requestBody);
                    batchTransactions = objectMapper.readValue(json,new TypeReference<List<BatchTransaction>>(){});
                } catch (Exception ex) {
                    LOGGER.error("Failed to Deserialize object");
                }

            batchResponder.ProduceBatchResponse(batchTransactions);
            break;

        }



        return new ResponseEntity<Void>(HttpStatus.OK);

    }




}
