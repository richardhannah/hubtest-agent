package com.richard.models.batchprocess;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by richard on 22/07/2016.
 */

public class BatchTransaction {
    
    private String action;
    private String partner;
    private String source;
    private String env;
    private int GPID;
    private String BTID;
    private String ship_method;
    private String shared_acct_id;
    private Boolean fake;
    private List<Vehicle> vehicles;
    private Contact contact;
    private Customer customer;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    @JsonProperty("GPID")
    public int getGPID() {
        return GPID;
    }

    public void setGPID(int GPID) {
        this.GPID = GPID;
    }

    @JsonProperty("BTID")
    public String getBTID() {
        return BTID;
    }

    public void setBTID(String BTID) {
        this.BTID = BTID;
    }

    public String getShip_method() {
        return ship_method;
    }

    public void setShip_method(String ship_method) {
        this.ship_method = ship_method;
    }

    public String getShared_acct_id() {
        return shared_acct_id;
    }

    public void setShared_acct_id(String shared_acct_id) {
        this.shared_acct_id = shared_acct_id;
    }

    public Boolean getFake() {
        return fake;
    }

    public void setFake(Boolean fake) {
        this.fake = fake;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public static class Vehicle{
        int IHID;
        String lastname;
        String firstname;
        String addr1;
        String addr2;
        String city;
        String state;
        String zip;
        String vin;
        String year;
        String make;
        String model;
        List<String> kits;
        String program_id;
        Boolean validVin;
        Boolean compatible;
        String options;
        String country;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        @JsonProperty("IHID")
        public int getIHID() {
            return IHID;
        }

        public void setIHID(int IHID) {
            this.IHID = IHID;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getAddr1() {
            return addr1;
        }

        public void setAddr1(String addr1) {
            this.addr1 = addr1;
        }

        public String getAddr2() {
            return addr2;
        }

        public void setAddr2(String addr2) {
            this.addr2 = addr2;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public String getVin() {
            return vin;
        }

        public void setVin(String vin) {
            this.vin = vin;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
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

        public List<String> getKits() {
            return kits;
        }

        public void setKits(List<String> kits) {
            this.kits = kits;
        }

        public String getProgram_id() {
            return program_id;
        }

        public void setProgram_id(String program_id) {
            this.program_id = program_id;
        }

        public Boolean getValidVin() {
            return validVin;
        }

        public void setValidVin(Boolean validVin) {
            this.validVin = validVin;
        }

        public Boolean getCompatible() {
            return compatible;
        }

        public void setCompatible(Boolean compatible) {
            this.compatible = compatible;
        }

        public String getOptions() {
            return options;
        }

        public void setOptions(String options) {
            this.options = options;
        }
    }
    
    public static class Contact{

        String primary_contact;
        String last_name;
        String first_name;
        String main_phone;
        String addr1;
        String addr2;
        String city;
        String state;
        String zip;
        String email;
/*
        @JsonCreator
        public Contact(
                @JsonProperty("primary_contact") String primary_contact,
                @JsonProperty("last_name") String last_name,
                @JsonProperty("first_name") String first_name,
                @JsonProperty("main_phone") String main_phone,
                @JsonProperty("addr1") String addr1,
                @JsonProperty("addr2") String addr2,
                @JsonProperty("city") String city,
                @JsonProperty("state") String state,
                @JsonProperty("zip") String zip,
                @JsonProperty("email") String email){

            this.primary_contact=primary_contact;
            this.last_name=last_name;
            this.first_name=first_name;
            this.main_phone=main_phone;
            this.addr1=addr1;
            this.addr2=addr2;
            this.city=city;
            this.state=state;
            this.zip=zip;
            this.email=email;
        }
*/
        public String getPrimary_contact() {
            return primary_contact;
        }

        public void setPrimary_contact(String primary_contact) {
            this.primary_contact = primary_contact;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getMain_phone() {
            return main_phone;
        }

        public void setMain_phone(String main_phone) {
            this.main_phone = main_phone;
        }

        public String getAddr1() {
            return addr1;
        }

        public void setAddr1(String addr1) {
            this.addr1 = addr1;
        }

        public String getAddr2() {
            return addr2;
        }

        public void setAddr2(String addr2) {
            this.addr2 = addr2;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class Customer{
        String alias;
        String trans_effect_date;
        String account_no;


        @JsonCreator
        public Customer(@JsonProperty("alias") String alias,
                        @JsonProperty("trans_effect_date") String trans_effect_date,
                        @JsonProperty("account_no") String account_no){

            this.alias=alias;
            this.trans_effect_date=trans_effect_date;
            this.account_no=account_no;

        }

    }

}


