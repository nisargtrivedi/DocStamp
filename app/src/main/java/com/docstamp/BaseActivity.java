package com.docstamp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.docstamp.Utils.AppPreferences;
import com.docstamp.WebApi.WebUtility;

import es.dmoral.toasty.Toasty;

public class BaseActivity extends AppCompatActivity
{

    AppPreferences appPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appPreferences=new AppPreferences(this);

        if(!TextUtils.isEmpty(appPreferences.getString("URL"))){
            WebUtility.BASE_URL=appPreferences.getString("URL");
        }
    }

    public void openAlert(String msg){

        Toasty.normal(this, msg, Toast.LENGTH_SHORT).show();

    }
    public void showSuccessMsg(String msg){
        Toasty.success(this, msg, Toast.LENGTH_SHORT, true).show();
    }
    public void showErrorMsg(String msg){
        Toasty.error(this, msg, Toast.LENGTH_SHORT, true).show();
    }
    public void showInfoMsg(String msg){
        Toasty.info(this, msg, Toast.LENGTH_SHORT, true).show();
    }
    public void showWarningMsg(String msg){
        Toasty.warning(this, msg, Toast.LENGTH_SHORT, true).show();
    }
    public void showNormalMsg(String msg){
        Toasty.normal(this, msg, Toast.LENGTH_SHORT).show();
    }

}
