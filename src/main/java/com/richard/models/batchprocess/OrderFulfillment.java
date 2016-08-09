package com.richard.models.batchprocess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class OrderFulfillment {
    private boolean fake;
    private String env;
    private String orderNo;
    private String soldToName;
    private String soldToAddr1;
    private String soldToAddr2;
    private String soldToAddr3;
    private String soldToCity;
    private String soldToState;
    private String soldToZip;
    private String soldToPhone;
    private String soldToCountry;
    private String shipVia;
    private List<OFVehicle> vehicles;
    private String action;
    private String carrier_program;

    private BatchTransaction batchTransaction;


    public OrderFulfillment() {}

    public OrderFulfillment(BatchTransaction batchTransaction){
        this.batchTransaction =batchTransaction;
        this.fake = true;
        this.env = "SB2";
        this.orderNo = "";
        this.soldToName = batchTransaction.getContact().getFirst_name() + " " +batchTransaction.getContact().getLast_name();
        this.soldToAddr1 = batchTransaction.getContact().getAddr1();
        this.soldToAddr2 = batchTransaction.getContact().getAddr2();
        this.soldToAddr3 = "";
        this.soldToCity = batchTransaction.getContact().getCity();
        this.soldToState = batchTransaction.getContact().getState();
        this.soldToCountry = "US";
        this.soldToZip = batchTransaction.getContact().getZip();
        this.soldToPhone = batchTransaction.getContact().getMain_phone();
        this.shipVia = "469";
        this.action = "order_device";
        this.carrier_program = batchTransaction.getPartner();

        this.vehicles = new ArrayList<>();
        this.vehicles.add(new OFVehicle(batchTransaction));



    }

    public OrderFulfillment(OrderFulfillment of, List<OFVehicle> vehicles) {
        this.fake = of.fake;
        this.env = of.env;
        this.orderNo = of.orderNo;
        this.soldToName = of.soldToName;
        this.soldToAddr1 = of.soldToAddr1;
        this.soldToAddr2 = of.soldToAddr2;
        this.soldToAddr3 = of.soldToAddr3;
        this.soldToCity = of.soldToCity;
        this.soldToState = of.soldToState;
        this.soldToZip = of.soldToZip;
        this.soldToPhone = of.soldToPhone;
        this.shipVia = of.shipVia;
        this.action = of.action;
        this.vehicles = vehicles;
    }

    public BatchTransaction getBatchTransaction(){
        return this.batchTransaction;
    }

    public String getSoldToCountry() {
        return soldToCountry;
    }

    public void setSoldToCountry(String soldToCountry) {
        this.soldToCountry = soldToCountry;
    }

    public String getCarrier_program() {
        return carrier_program;
    }

    public void setCarrier_program(String carrier_program) {
        this.carrier_program = carrier_program;
    }

    //public boolean isVehicleUpdate() {
    //    return "update_vehicle".equalsIgnoreCase(action);
    //}

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSoldToName() {
        return soldToName;
    }

    public void setSoldToName(String soldToName) {
        this.soldToName = soldToName;
    }

    public String getSoldToAddr1() {
        return soldToAddr1;
    }

    public void setSoldToAddr1(String soldToAddr1) {
        this.soldToAddr1 = soldToAddr1;
    }

    public String getSoldToAddr2() {
        return soldToAddr2;
    }

    public void setSoldToAddr2(String soldToAddr2) {
        this.soldToAddr2 = soldToAddr2;
    }

    public String getSoldToAddr3() {
        return soldToAddr3;
    }

    public void setSoldToAddr3(String soldToAddr3) {
        this.soldToAddr3 = soldToAddr3;
    }

    public String getSoldToCity() {
        return soldToCity;
    }

    public void setSoldToCity(String soldToCity) {
        this.soldToCity = soldToCity;
    }

    public String getSoldToState() {
        return soldToState;
    }

    public void setSoldToState(String soldToState) {
        this.soldToState = soldToState;
    }

    public String getSoldToZip() {
        return soldToZip;
    }

    public void setSoldToZip(String soldToZip) {
        this.soldToZip = soldToZip;
    }

    public String getSoldToPhone() {
        return soldToPhone;
    }

    public void setSoldToPhone(String soldToPhone) {
        this.soldToPhone = soldToPhone;
    }

    public String getShipVia() {
        return shipVia;
    }

    public void setShipVia(String shipVia) {
        this.shipVia = shipVia;
    }

    public List<OFVehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<OFVehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public boolean isFake() {
        return fake;
    }

    public void setFake(boolean fake) {
        this.fake = fake;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "OrderFulfillment [fake=" + fake + ", env=" + env + ", orderNo=" + orderNo + ", soldToName=" + soldToName + ", soldToAddr1="
                + soldToAddr1 + ", soldToAddr2=" + soldToAddr2 + ", soldToCity=" + soldToCity + ", soldToState=" + soldToState
                + ", soldToZip=" + soldToZip + ", soldToPhone=" + soldToPhone + ", shipVia=" + shipVia + ", vehicles="
                + vehicles + "]";
    }
}
