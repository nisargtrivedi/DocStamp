package com.docstamp.ApiController;

import com.docstamp.Model.tblClients;
import com.docstamp.Model.tblFiles;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ClientUrlResponse extends BaseResponse {

    @SerializedName("Response")
    public tblClients clients;

    @Override
    public boolean isValid() {
        if(message.Error==1)
            return false;
        else
            return true;
    }
}
