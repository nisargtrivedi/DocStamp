package com.docstamp.ApiController;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public abstract class BaseResponse implements Serializable {

    @SerializedName("msg")
    public Message message;

    public abstract boolean isValid();
}
