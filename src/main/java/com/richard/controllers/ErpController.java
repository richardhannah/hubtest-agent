package com.richard.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.richard.models.batchprocess.BatchResponse;
import com.richard.models.batchprocess.BatchTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 21/07/2016.
 */
@RestController
@RequestMapping("/erp")
public class ErpController {

//@RequestMapping(value= "accounts", method=RequestMethod.POST, consumes={com.himexubi.himexubiobjects.domain.model.v1.authentication.Account.MEDIA_TYPE})

    private static Logger LOGGER = LogManager.getLogger(ErpController.class);

    @Autowired
    RestTemplate inthubRestTemplate;

    @Autowired
    NamedParameterJdbcTemplate jdbcInternalTemplate;

    @RequestMapping("/hello")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping(value = "/netsuite", method = RequestMethod.POST)

    public void netsuite(@RequestBody List<BatchTransaction> requestBody){
        ObjectMapper mapper =new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,true);

        String json ="";
        try{
            json = mapper.writeValueAsString(requestBody);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        jdbcInternalTemplate.update("INSERT into hubtest_agent.account (raw) values (:json)",new MapSqlParameterSource().addValue("json",json));
        jdbcInternalTemplate.update("INSERT into hubtest_agent.vehicle (raw) values (:json)",new MapSqlParameterSource().addValue("json",json));


        LOGGER.trace("Batch Transaction recieved :" + json);


        List<BatchResponse> batchResponses = new ArrayList<>();

        for(BatchTransaction transaction : requestBody) {

            BatchResponse response = new BatchResponse();
            response.setGPID(transaction.getGPID());
            response.setIHID(transaction.getVehicles().get(0).getIHID());
            response.setNSID(NSIDBuilder(transaction.getPartner()));
            response.setBTID(Integer.parseInt(transaction.getBTID()));
            response.setErrorCode(0);
            response.setPartner_id(transaction.getPartner());



            try{
                json = mapper.writeValueAsString(response);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }

            LOGGER.trace("Sending response: " + json);

            batchResponses.add(response);


        }
        inthubRestTemplate.postForObject("http://localhost:8080/Integration-Hub/br/upload", batchResponses,List.class);

    }

    private String NSIDBuilder(String partnerId){


        int accountId = jdbcInternalTemplate.queryForObject("SELECT id FROM hubtest_agent.account order by id desc limit 1;",new MapSqlParameterSource(), Integer.class);
        int vehicleId = jdbcInternalTemplate.queryForObject("SELECT id FROM hubtest_agent.account order by id desc limit 1;",new MapSqlParameterSource(), Integer.class);


        return accountId+":"+partnerId+":"+vehicleId;
    }


}
