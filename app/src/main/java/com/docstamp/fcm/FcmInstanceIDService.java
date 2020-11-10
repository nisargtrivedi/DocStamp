package com.docstamp.fcm;

import android.util.Log;

import com.docstamp.Utils.AppPreferences;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class FcmInstanceIDService extends FirebaseInstanceIdService {

    AppPreferences sharedPref;

    @Override
    public void onTokenRefresh() {
        sharedPref = new AppPreferences(this);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        sharedPref.set("DEVICE_KEY",token);
        Log.i("TOKEN =:",token+"");
        //sharedPref.setOpenAppCounter(SharedPref.MAX_OPEN_COUNTER);
    }
}
