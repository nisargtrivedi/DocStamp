package com.docstamp.ApiController;

import com.docstamp.Model.tblFiles;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class FilesAndGroupFileResponse implements Serializable {

    @SerializedName("GroupFiles")
    public ArrayList<com.docstamp.Model.tblFiles> tblGroupFiles;

    @SerializedName("Single")
    public ArrayList<com.docstamp.Model.tblFiles> tblFiles;
}
