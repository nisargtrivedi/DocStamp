package com.docstamp.ApiController;

import com.docstamp.Model.tblFolder;
import com.docstamp.Model.tblYear;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class YearResponse extends BaseResponse {

    @SerializedName("Response")
    public ArrayList<tblYear> tblYears;
    @Override
    public boolean isValid() {
        if(message.Error==1)
            return false;
        else
            return true;
    }
}
