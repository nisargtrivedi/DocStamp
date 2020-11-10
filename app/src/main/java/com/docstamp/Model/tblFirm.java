package com.docstamp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class tblFirm implements Serializable {


    public int FirmID;
    public String FirmName;
    public int Image;

    public tblFirm(int ID,String name,int img){
        this.FirmID=ID;
        this.FirmName=name;
        this.Image=img;
    }

}
