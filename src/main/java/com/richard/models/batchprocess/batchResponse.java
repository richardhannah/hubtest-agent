package com.richard.models.batchprocess;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by richard on 26/07/2016.
 */
public class BatchResponse {

    private int GPID;
    private int IHID;
    private String NSID;
    private int BTID;
    private int errorCode;
    private String partner_id;

    @JsonProperty("GPID")
    public int getGPID() {
        return GPID;
    }

    public void setGPID(int GPID) {
        this.GPID = GPID;
    }

    @JsonProperty("IHID")
    public int getIHID() {
        return IHID;
    }

    public void setIHID(int IHID) {
        this.IHID = IHID;
    }

    @JsonProperty("NSID")
    public String getNSID() {
        return NSID;
    }

    public void setNSID(String NSID) {
        this.NSID = NSID;
    }

    @JsonProperty("BTID")
    public int getBTID() {
        return BTID;
    }

    public void setBTID(int BTID) {
        this.BTID = BTID;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(String partner_id) {
        this.partner_id = partner_id;
    }
}
