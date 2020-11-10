package com.docstamp.ApiController;

import com.docstamp.Model.tblFiles;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class KnowledgeFilesResponse extends BaseResponse {

    @SerializedName("Response")
    public ArrayList<com.docstamp.Model.tblFiles> filesObj;



    @Override
    public boolean isValid() {
        if(message.Error==1)
            return false;
        else
            return true;
    }
}
