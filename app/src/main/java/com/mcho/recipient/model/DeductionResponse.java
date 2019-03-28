package com.mcho.recipient.model;

import com.google.gson.annotations.SerializedName;

public class DeductionResponse {

    @SerializedName("success")
    private Integer success;
    @SerializedName("remaining")
    private Integer remaining;
    @SerializedName("msg")
    private String msg;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getRemaining() {
        return remaining;
    }

    public void setRemaining(Integer remaining) {
        this.remaining = remaining;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
