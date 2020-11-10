package com.docstamp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class tblYear implements Serializable {

    @SerializedName("year_id")
    public int YearID;

    @SerializedName("year_name")
    public String YearName;

}
