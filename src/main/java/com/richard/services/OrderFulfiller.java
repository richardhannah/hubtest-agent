package com.richard.services;

import com.richard.models.batchprocess.OrderFulfillment;
import com.richard.models.batchprocess.TermsAndRegAcceptance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by richard on 04/08/2016.
 */
public class OrderFulfiller {

    private static Logger LOGGER = LogManager.getLogger(OrderFulfiller.class);


    @Autowired
    RestTemplate inthubRestTemplate;

    @Autowired
    NamedParameterJdbcTemplate jdbcInternalTemplate;


    public void produceOrder(int customerId){

        LOGGER.trace("producing order");


        Thread thread = new Thread(new FulfillOrderRunnable(customerId,inthubRestTemplate,jdbcInternalTemplate));
        thread.start();




    }

    /*



     */


}
