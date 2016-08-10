package com.richard.services;

import com.richard.dal.HubtestAgentDao;
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
    HubtestAgentDao hubtestAgentDao;

    @Autowired
    ItemService itemService;


    public void produceOrder(int customerId){

        LOGGER.trace("producing order");
        Thread thread = new Thread(new FulfillOrderRunnable(customerId,inthubRestTemplate,hubtestAgentDao,itemService));
        thread.start();

    }

    public void processTermsAccept(int customerId){
        hubtestAgentDao.processTermsAccept(customerId);
    }

    public void processRegistration(int customerId){
        hubtestAgentDao.processRegistration(customerId);
    }

    public boolean checkAcceptance(TermsAndRegAcceptance termsAndRegAcceptance){

        List<Map<String,Object>> rows = hubtestAgentDao.fetchCustomer(Integer.valueOf(termsAndRegAcceptance.getCustomer_id()));
        boolean bothAccepted = false;
        for(Map row : rows){
            bothAccepted = (Boolean)row.get("registered") && (Boolean)row.get("terms_accepted");
        }
        return bothAccepted;
    }




}
