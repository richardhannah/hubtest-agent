package com.richard.models.batchprocess;

/**
 * Created by richard on 01/08/2016.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class OFVehicle {

    private List<OFItem> items;
    private String vehicle_id;
    private String make;
    private String model;
    private String year;
    //private String esn;
    private String order_notes;
    private String carrier_program;
    private String orig_year;
    private String orig_make;
    private String orig_model;
    private String orig_vin;
    private String vin;


    public OFVehicle(BatchTransaction batchTransaction){

        items = new ArrayList<>();
        items.add(new OFItem());

        this.vehicle_id = "ENR-4408756:1:2245280-XOAXGARBAGEXVINXO:Item001,ENR-4408756:1";
        this.make = batchTransaction.getVehicles().get(0).getMake();
        this.model = batchTransaction.getVehicles().get(0).getModel();
        this.year = batchTransaction.getVehicles().get(0).getYear();
        this.order_notes = "2006 Ford Focus XOAXGARBAGEXVINXO";
        this.carrier_program = batchTransaction.getPartner();
        this.orig_make = batchTransaction.getVehicles().get(0).getMake();
        this.orig_model = batchTransaction.getVehicles().get(0).getModel();
        this.orig_year = batchTransaction.getVehicles().get(0).getYear();
        this.vin = batchTransaction.getVehicles().get(0).getVin();
        this.orig_vin = batchTransaction.getVehicles().get(0).getVin();



    }

    public String getOrig_vin() {
        return orig_vin;
    }

    public void setOrig_vin(String orig_vin) {
        this.orig_vin = orig_vin;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getCarrier_program() {
        return carrier_program;
    }

    public void setCarrier_program(String carrier_program) {
        this.carrier_program = carrier_program;
    }

    public List<OFItem> getItems() {
        return items;
    }

    public void setItems(List<OFItem> items) {
        this.items = items;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    //public String getEsn() {
    //    return esn;
    //}

    //public void setEsn(String esn) {
    //    this.esn = esn;
    //}

    public String getOrder_notes() {
        return order_notes;
    }

    public void setOrder_notes(String order_notes) {
        this.order_notes = order_notes;
    }

    public String getOrig_year() {
        return orig_year;
    }

    public void setOrig_year(String orig_year) {
        this.orig_year = orig_year;
    }

    public String getOrig_make() {
        return orig_make;
    }

    public void setOrig_make(String orig_make) {
        this.orig_make = orig_make;
    }

    public String getOrig_model() {
        return orig_model;
    }

    public void setOrig_model(String orig_model) {
        this.orig_model = orig_model;
    }

    @Override
    public String toString() {
        return "OFVehicle{" +
                "items=" + items.toString() +
                ", vehicle_id='" + vehicle_id + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year='" + year + '\'' +
                ", esn='" + "esn" + '\'' +
                ", order_notes='" + order_notes + '\'' +
                ", orig_year='" + orig_year + '\'' +
                ", orig_make='" + orig_make + '\'' +
                ", orig_model='" + orig_model + '\'' +
                '}';
    }
}

