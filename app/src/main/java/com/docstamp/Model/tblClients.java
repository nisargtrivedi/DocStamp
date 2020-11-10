package com.docstamp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class tblClients implements Serializable {


    @SerializedName("client_id")
    public int ClientID;
    @SerializedName("client_name")
    public String ClientName;
    @SerializedName("client_url")
    public String ClientURL;

    public tblClients(int ID, String name, String url){
        this.ClientID=ID;
        this.ClientName=name;
        this.ClientURL=url;
    }

}
