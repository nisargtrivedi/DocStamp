package com.docstamp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class tblUser implements Serializable {

    @SerializedName("user_id")
    public int UserID;

    @SerializedName("first_name")
    public String FirstName;

    @SerializedName("last_name")
    public String last_name;

    @SerializedName("city")
    public String City;

    @SerializedName("email")
    public String Email;

    @SerializedName("phone")
    public String Phone;

    @SerializedName("token")
    public String Token;

    @SerializedName("img")
    public String Image;

    @SerializedName("notification")
    public int Notification;
}


//{"Response":
// [
    // {"user_id":1,
    // "first_name":"Nisarg",
    // "last_name":"Trivedi",
    // "city":"ahmedabad",
    // "email":"nisarg@gmail.com",
    // "phone":"9978538694",
    // "token":"123"}
// ],
// "msg":{"error":0,"message":"User Details"}}