package com.docstamp.ApiController;

import com.docstamp.Model.tblFolder;
import com.docstamp.Model.tblUser;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FolderResponse extends BaseResponse {

    @SerializedName("Response")
    public ArrayList<tblFolder> tblFolders;
    @Override
    public boolean isValid() {
        if(message.Error==1)
            return false;
        else
            return true;
    }
}
