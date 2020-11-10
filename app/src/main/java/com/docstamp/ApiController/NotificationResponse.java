package com.docstamp.ApiController;

import com.docstamp.Model.tblFiles;
import com.docstamp.Model.tblNotification;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NotificationResponse extends BaseResponse {

    @SerializedName("Response")
    public ArrayList<tblNotification> tblNotifications;
    @Override
    public boolean isValid() {
        if(message.Error==1)
            return false;
        else
            return true;
    }
}
