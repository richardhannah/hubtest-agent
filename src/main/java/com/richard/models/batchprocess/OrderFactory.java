package com.richard.models.batchprocess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 10/08/2016.
 */
public class OrderFactory {

    private int orderReference;
    private String orderNo;
    private OFItem ofItem;
    private BatchTransaction.Vehicle vehicle;
    private int vehicleReference;
    private BatchTransaction.Contact contact;

    public OrderFactory(int orderReference){
        this.orderReference=orderReference;
    }

    public OrderFulfillment getOrderFulfillment(){

        List<OFVehicle> ofVehicles = new ArrayList<>();
        ofVehicles.add(ofVehicle());

        OrderFulfillment orderFulfillment = new OrderFulfillment();
        orderFulfillment.setVehicles(ofVehicles);
        orderFulfillment.setFake(true);
        orderFulfillment.setEnv("SB2");
        orderFulfillment.setOrderNo(orderNo);
        orderFulfillment.setSoldToName(vehicle.getFirstname() + " " + vehicle.getLastname());
        orderFulfillment.setSoldToAddr1(vehicle.getAddr1());
        orderFulfillment.setSoldToAddr2(vehicle.getAddr2());
        orderFulfillment.setSoldToAddr3("");
        orderFulfillment.setSoldToCity(vehicle.getCity());
        orderFulfillment.setSoldToState(vehicle.getState());
        orderFulfillment.setSoldToZip(vehicle.getZip());
        orderFulfillment.setSoldToPhone(contact.getMain_phone());
        orderFulfillment.setSoldToCountry(vehicle.getCountry());
        orderFulfillment.setShipVia("469");
        orderFulfillment.setAction("order_device");
        orderFulfillment.setCarrier_program(vehicle.getProgram_id());

        return orderFulfillment;
    }

    public void setContact(BatchTransaction.Contact contact){
        this.contact=contact;
    }

    public void setOrderReference(int orderReference) {
        this.orderReference = orderReference;
        this.orderNo = "ENR-"+orderReference+":1";
    }

    public void setOfItem(OFItem ofItem) {
        this.ofItem = ofItem;
    }

    public void setVehicle(BatchTransaction.Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setVehicleReference(int vehicleReference) {
        this.vehicleReference = vehicleReference;
    }

    private String orderNotes(){
        return String.format("%s %s %s %s", vehicle.getYear(), vehicle.getMake(), vehicle.getModel(), vehicle.getVin());
    }

    private String vehicleId(){
        return String.format("%s:%d-%s:Item001,%s",orderNo,vehicleReference,vehicle.getVin(),orderNo);
    }

    private OFVehicle ofVehicle(){
        OFVehicle ofVehicle = new OFVehicle();
        ofVehicle.setMake(vehicle.getMake());
        ofVehicle.setModel(vehicle.getModel());
        ofVehicle.setYear(vehicle.getYear());
        ofVehicle.setVin(vehicle.getVin());
        ofVehicle.setOrig_make(vehicle.getMake());
        ofVehicle.setOrig_model(vehicle.getModel());
        ofVehicle.setOrig_year(vehicle.getYear());
        ofVehicle.setOrig_vin(vehicle.getVin());
        ofVehicle.setCarrier_program(vehicle.getProgram_id());
        ofVehicle.setOrder_notes(orderNotes());
        ofVehicle.setVehicle_id(vehicleId());

        ofVehicle.setItems(new ArrayList<OFItem>());
        ofVehicle.getItems().add(ofItem);
        return ofVehicle;
    }


}
