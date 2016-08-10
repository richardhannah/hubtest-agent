package com.richard.services;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.richard.models.batchprocess.BatchTransaction;
import com.richard.models.batchprocess.OrderFulfillment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.client.RestTemplate;
import com.richard.utils.Tools;

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
    private BatchTransaction batchTransaction;

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
        batchTransaction = fetchBatchTransaction(customerId);

        Tools.wait(5000);
        OrderFulfillment orderFulfillment = new OrderFulfillment(batchTransaction);
        persistOrder(orderFulfillment);
        String orderId = generateOrderId(orderFulfillment);

        orderFulfillment.getVehicles().get(0).setVehicle_id(generateVehicleId(orderId,batchTransaction));

        try {
            LOGGER.trace(mapper.writeValueAsString(orderFulfillment));
        }
        catch (Exception ex){
            LOGGER.error("Json processing exception",ex);
        }

        List<OrderFulfillment> orderFulfillments = new ArrayList<>();
        orderFulfillments.add(orderFulfillment);

        ResponseEntity<String> resp = inthubRestTemplate.postForEntity("http://localhost:8080/Integration-Hub/tpl/placeorder/TPL1", orderFulfillments,String.class);
        LOGGER.trace(resp.getBody());

    }

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


}
