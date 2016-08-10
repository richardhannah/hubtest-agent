package com.richard.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.richard.models.batchprocess.BatchResponse;
import com.richard.models.batchprocess.BatchTransaction;
import com.richard.models.batchprocess.TermsAndRegAcceptance;
import com.richard.services.BatchResponder;
import com.richard.services.OrderFulfiller;
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

    @Autowired
    OrderFulfiller orderFulfiller;

    @Autowired
    NamedParameterJdbcTemplate jdbcInternalTemplate;



    @RequestMapping("/hello")
    public String index() {
        return "Greetings from Spring Boot!";
    }



    @RequestMapping(value = "/netsuite", method = RequestMethod.POST)
    public ResponseEntity<Void> netsuite (@RequestBody Object requestBody, @RequestParam int script){
        LOGGER.trace("Script id: " + script);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            LOGGER.trace("Request Recieved : "+objectMapper.writeValueAsString(requestBody));
        } catch (Exception ex) {
            LOGGER.error("Failed to Deserialize object");
        }

        switch (script){
            case 81:
                List<BatchTransaction> batchTransactions = new ArrayList<>();
                try {
                    String json = objectMapper.writeValueAsString(requestBody);
                    batchTransactions = objectMapper.readValue(json,new TypeReference<List<BatchTransaction>>(){});
                } catch (Exception ex) {
                    LOGGER.error("Failed to Deserialize object");
                }

            batchResponder.ProduceBatchResponse(batchTransactions);
            break;
            case 54:
                TermsAndRegAcceptance termsAndRegAcceptance = new TermsAndRegAcceptance();
                try {
                    String json = objectMapper.writeValueAsString(requestBody);
                    termsAndRegAcceptance = objectMapper.readValue(json,TermsAndRegAcceptance.class);
                } catch (Exception ex) {
                    LOGGER.error("Failed to Deserialize object");
                }

                /*
                if(termsAndRegAcceptance.getAccepted_tcs() != null){
                    LOGGER.trace("terms and conditions accepted");
                    jdbcInternalTemplate.update("UPDATE hubtest_agent.account SET terms_accepted=1 WHERE id=:id", new MapSqlParameterSource().addValue("id", termsAndRegAcceptance.getCustomer_id()));
                }

                if(termsAndRegAcceptance.getFirst_registered() !=null){
                    LOGGER.trace("registered");
                    jdbcInternalTemplate.update("UPDATE hubtest_agent.account SET registered=1 WHERE id=:id", new MapSqlParameterSource().addValue("id", termsAndRegAcceptance.getCustomer_id()));
                }

                if(checkAcceptance(termsAndRegAcceptance)){
                    LOGGER.trace("both accepted - generating order");
                    orderFulfiller.produceOrder(Integer.valueOf(termsAndRegAcceptance.getCustomer_id()));

                }
                */



            break;

        }



        return new ResponseEntity<Void>(HttpStatus.OK);

    }

    private boolean checkAcceptance(TermsAndRegAcceptance termsAndRegAcceptance){

        List<Map<String,Object>> rows = jdbcInternalTemplate.queryForList("SELECT * FROM hubtest_agent.account WHERE id=:id", new MapSqlParameterSource().addValue("id", termsAndRegAcceptance.getCustomer_id()));
        boolean bothAccepted = false;
        for(Map row : rows){
            bothAccepted = (Boolean)row.get("registered") && (Boolean)row.get("terms_accepted");
        }
        return bothAccepted;
    }




}
