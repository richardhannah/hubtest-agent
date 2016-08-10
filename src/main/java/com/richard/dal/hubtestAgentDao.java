package com.richard.dal;

import com.richard.models.batchprocess.BatchTransaction;
import com.richard.services.BatchResponder;
import com.richard.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by richard on 10/08/2016.
 */
public class HubtestAgentDao {

    @Autowired
    NamedParameterJdbcTemplate jdbcInternalTemplate;

    private static String INSERT_BATCH_TRANSACTION = "INSERT INTO hubtest_agent.batchtransaction (raw,ihid,btid,gpid) VALUES (:raw,:ihid,:btid,:gpid)";
    private static String INSERT_CUSTOMER = "INSERT INTO hubtest_agent.customer (raw,carrier_account_no) values (:json,:carrier_account_no)";
    private static String INSERT_VEHICLE = "INSERT INTO hubtest_agent.vehicle (raw,customer_id,ihid) values (:json,:customer_id,:ihid)";
    private static String SELECT_VEHICLE_BY_IHID = "SELECT id FROM hubtest_agent.vehicle WHERE ihid=:ihid;";
    private static String SELECT_CUSTOMER_BY_ACCTNO = "SELECT id FROM hubtest_agent.customer WHERE carrier_account_no=:carrier_acct_no;";
    private static String INSERT_TERMS_ACCEPT = "UPDATE hubtest_agent.customer SET terms_accepted=1 WHERE id=:id";
    private static String INSERT_REGISTRATION = "UPDATE hubtest_agent.customer SET registered=1 WHERE id=:id";
    private static String SELECT_CUSTOMER = "SELECT * FROM hubtest_agent.customer WHERE id=:id";
    private static String INSERT_BLANK_ORDER = "INSERT INTO hubtest_agent.order (raw,order_id,vin) values(null,null,null)";
    private static String SELECT_VEHICLES_WITH_UNSENT_ORDERS = "SELECT * FROM hubtest_agent.vehicle WHERE customer_id=:customer_id AND order_sent=0";
    private static String SELECT_CUSTOMER_RAW = "SELECT raw FROM hubtest_agent.customer WHERE id=:id";


    public void saveTransaction(BatchTransaction batchTransaction){

        for(BatchTransaction.Vehicle vehicle : batchTransaction.getVehicles()){
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("raw", Tools.serialize(batchTransaction));
            params.addValue("ihid", vehicle.getIHID());
            params.addValue("btid", batchTransaction.getBTID());
            params.addValue("gpid", batchTransaction.getGPID());
            jdbcInternalTemplate.update(INSERT_BATCH_TRANSACTION,params);
        }
    }

    public int createCustomer(BatchTransaction batchTransaction){

        MapSqlParameterSource customerParams = new MapSqlParameterSource();
        customerParams.addValue("json",Tools.serialize(batchTransaction));
        customerParams.addValue("carrier_account_no",batchTransaction.getCustomer().getAccount_no());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcInternalTemplate.update(INSERT_CUSTOMER, customerParams,keyHolder);
        return keyHolder.getKey().intValue();
    }

    public void createVehicle(int customerId, BatchTransaction.Vehicle vehicle){

        MapSqlParameterSource vehParams = new MapSqlParameterSource();
        vehParams.addValue("json", Tools.serialize(vehicle));
        vehParams.addValue("customer_id", customerId);
        vehParams.addValue("ihid", vehicle.getIHID());
        jdbcInternalTemplate.update(INSERT_VEHICLE, vehParams);

    }

    public int fetchVehicleIdByIhid(int ihid){
        return jdbcInternalTemplate.queryForObject(SELECT_VEHICLE_BY_IHID,new MapSqlParameterSource().addValue("ihid",ihid), Integer.class);
    }

    public int fetchCustomerByAcctNo(int carrierAcctNo){
        return jdbcInternalTemplate.queryForObject(SELECT_CUSTOMER_BY_ACCTNO, new MapSqlParameterSource().addValue("carrier_acct_no",carrierAcctNo),Integer.class);
    }

    public void processTermsAccept(int customerId){
        jdbcInternalTemplate.update(INSERT_TERMS_ACCEPT, new MapSqlParameterSource().addValue("id", customerId));
    }
    public void processRegistration(int customerId){
        jdbcInternalTemplate.update(INSERT_REGISTRATION, new MapSqlParameterSource().addValue("id", customerId));
    }

    public List<Map<String,Object>> fetchCustomer(int customerId){
        return jdbcInternalTemplate.queryForList(SELECT_CUSTOMER, new MapSqlParameterSource().addValue("id", customerId));
    }

    public List<Map<String,Object>> fetchVehiclesWithOrdersToBeSent(int customerId){
        return jdbcInternalTemplate.queryForList(SELECT_VEHICLES_WITH_UNSENT_ORDERS, new MapSqlParameterSource().addValue("customer_id", customerId));
    }

    public int createNewOrder(){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcInternalTemplate.update(INSERT_BLANK_ORDER, new MapSqlParameterSource(),keyHolder);
        return keyHolder.getKey().intValue();
    }

    public String getCustomerRaw(){
        re
    }

}
