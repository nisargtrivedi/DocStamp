package com.docstamp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class tblFiles implements Serializable {

    @SerializedName("folder_id")
    public String FolderID;

    @SerializedName("folder_name")
    public String FolderName;

    @SerializedName("document_title")
    public String DocumentTitle;

    @SerializedName("download_path")
    public String DownloadPath;

    @SerializedName("filesize")
    public String FileSize;

    @SerializedName("file_upload_time")
    public String FileUploadTime;



}

//{"Response":[{"folder_id":1,"folder_name":"it return","document_title":"test","download_path":"http:\/\/v-yukti.com\/documentapp\/data\/1_041604.png","filesize":"258.68 KB","file_upload_time":"2019-06-17 04:16:04"},{"folder_id":1,"folder_name":"it return","document_title":"test","download_path":"http:\/\/v-yukti.com\/documentapp\/data\/1_042017.png","filesize":"258.68 KB","file_upload_time":"2019-06-17 04:20:17"}],"msg":{"error":0,"message":"Files For Users are Below"}}
