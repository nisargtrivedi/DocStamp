package com.docstamp.ApiController;

import com.docstamp.Model.tblFiles;
import com.docstamp.Model.tblFolder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FilesResponse extends BaseResponse {

    @SerializedName("Response")
    public FilesAndGroupFileResponse tblFiles;



    @Override
    public boolean isValid() {
        if(message.Error==1)
            return false;
        else
            return true;
    }
}
