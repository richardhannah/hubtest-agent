package com.richard.models.batchprocess;

/**
 * Created by richard on 04/08/2016.
 */
public class TermsAndRegAcceptance {
    private String customer_id;
    private String accepted_tcs;
    private String first_registered;

    public String getAccepted_tcs() {
        return accepted_tcs;
    }

    public void setAccepted_tcs(String accepted_tcs) {
        this.accepted_tcs = accepted_tcs;
    }

    public String getFirst_registered() {
        return first_registered;
    }

    public void setFirst_registered(String first_registered) {
        this.first_registered = first_registered;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }
}
