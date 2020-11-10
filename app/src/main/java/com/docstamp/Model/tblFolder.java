package com.docstamp.Model;

import com.docstamp.ApiController.BaseResponse;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class tblFolder implements Serializable {

    @SerializedName("folder_id")
    public int FolderID;

    @SerializedName("folder_name")
    public String FolderName;

    @SerializedName("files")
    public int Files;

    @SerializedName("folder_icon")
    public String FolderIcon;

}
