package com.richard.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.richard.dal.HubtestAgentDao;
import com.richard.models.batchprocess.BatchTransaction;
import com.richard.models.batchprocess.OrderFactory;
import com.richard.utils.Tools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * Created by richard on 08/08/2016.
 */
public class FulfillOrderRunnable implements Runnable {

    private static Logger LOGGER = LogManager.getLogger(FulfillOrderRunnable.class);

    private int customerId;
    private RestTemplate inthubRestTemplate;
    private HubtestAgentDao hubtestAgentDao;
    private ItemService itemService;
    private ObjectMapper mapper;


    private BatchTransaction batchTransaction;

    public FulfillOrderRunnable() {
    }

    public FulfillOrderRunnable(int customerId,RestTemplate inthubRestTemplate, HubtestAgentDao hubtestAgentDao,ItemService itemService) {
        this.customerId = customerId;
        this.inthubRestTemplate = inthubRestTemplate;
        this.hubtestAgentDao = hubtestAgentDao;
        this.itemService=itemService;
        mapper = new ObjectMapper();
    }

    @Override
    public void run() {
        LOGGER.trace("fulfill order thread started");
        OrderFactory orderFactory = new OrderFactory(customerId);
        List<Map<String,Object>> vehicleRows = hubtestAgentDao.fetchVehiclesWithOrdersToBeSent(customerId);


        for(Map<String,Object> vehicleRow : vehicleRows){
            orderFactory.setOrderReference(newOrder());
            orderFactory.setOfItem(itemService.retrieveItem());
            orderFactory.setVehicle(reconstructVehicle(vehicleRow));
            orderFactory.setVehicleReference((Integer)vehicleRow.get("id"));
        }
        LOGGER.trace(Tools.serialize(orderFactory.getOrderFulfillment()));
    }

    private int newOrder(){
        return hubtestAgentDao.createNewOrder();
    }

    private BatchTransaction.Vehicle reconstructVehicle(Map<String,Object> vehicleRow) {
        BatchTransaction.Vehicle vehicle = null;
        try{
            vehicle = mapper.readValue((String)vehicleRow.get("raw"),BatchTransaction.Vehicle.class);
        }
        catch (Exception ex){
            LOGGER.error(ex);
        }

        return vehicle;
    }

    private BatchTransaction.Contact reconstructContact(String contactJson){

    }

}


/*
    private String generateVehicleId(String orderId,BatchTransaction batchTransaction){

        return "ENR-"+orderId+":"+fetchVehicleId(customerId)+"-"+batchTransaction.getVehicles().get(0).getVin()+":Item001,"+orderId;

    }

    private int fetchVehicleId(int customerId){
        return jdbcInternalTemplate.queryForObject("SELECT id FROM hubtest_agent.vehicle WHERE account_id=:account_id ORDER BY id DESC LIMIT 1",new MapSqlParameterSource().addValue("account_id",customerId),Integer.class);
    }

    private String generateOrderId(OrderFulfillment orderFulfillment){
        String result = jdbcInternalTemplate.queryForObject("SELECT id FROM hubtest_agent.orders WHERE vin=:vin",new MapSqlParameterSource().addValue("vin", orderFulfillment.getVehicles().get(0).getVin()),String.class);
        String orderId = "ENR-"+result+":1";
        LOGGER.trace("order id is :" + orderId);
        jdbcInternalTemplate.update("INSERT into hubtest_agent.orders (order_id) values (:order_id)", new MapSqlParameterSource().addValue(":order_id",orderId));
        return orderId;
    }


    private void persistOrder(OrderFulfillment orderFulfillment){
        String json = "";
        try {
            json = mapper.writeValueAsString(orderFulfillment);
        } catch (Exception ex) {
            LOGGER.error("Failed to Deserialize object");
        }

        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("json",json);
        sqlParameterSource.addValue("vin", orderFulfillment.getVehicles().get(0).getVin());

        jdbcInternalTemplate.update("INSERT into hubtest_agent.orders (raw,vin) values (:json,:vin)", sqlParameterSource);

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
    */
