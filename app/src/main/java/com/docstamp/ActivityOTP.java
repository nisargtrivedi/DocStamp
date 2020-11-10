package com.docstamp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.docstamp.ApiController.LoginResponse;
import com.docstamp.Utils.EEditText;
import com.docstamp.Utils.TTextView;
import com.docstamp.Utils.Utility;
import com.docstamp.WebApi.WebServiceCaller;
import com.docstamp.WebApi.WebUtility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityOTP extends BaseActivity implements View.OnClickListener {

    TTextView btnChangeNumber,btnResend;
    EEditText edtno1,edtno2,edtno3,edtno4;

    String phone="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        edtno4=findViewById(R.id.edtno4);
        edtno3=findViewById(R.id.edtno3);
        edtno2=findViewById(R.id.edtno2);
        edtno1=findViewById(R.id.edtno1);
        btnChangeNumber=findViewById(R.id.btnChangeNumber);
        btnResend=findViewById(R.id.btnResend);

        phone=getIntent().getStringExtra("phone");
        btnResend.setOnClickListener(this);
        btnChangeNumber.setOnClickListener(this);

        edtno1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edtno1.getText().length()>0){
                    edtno2.requestFocus();
                }
            }
        });
        edtno2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edtno2.getText().length()>0){
                    edtno3.requestFocus();
                }
            }
        });
        edtno3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edtno3.getText().length()>0){
                    edtno4.requestFocus();
                }
            }
        });
       edtno4.setOnEditorActionListener(new TextView.OnEditorActionListener() {
           @Override
           public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
               if(actionId== EditorInfo.IME_ACTION_DONE){
                    String otp=edtno1.getText().toString()+edtno2.getText().toString()+edtno3.getText().toString()+edtno4.getText().toString();
                    if(!otp.isEmpty())
                        APICALL(otp);

                    return true;
               }else
                    return false;
           }
       });

    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnChangeNumber:
                    finish();
                    startActivity(new Intent(ActivityOTP.this,Register.class));
                    break;
                case R.id.btnResend:
                    RESENDAPICALL();
                    break;
            }
    }
    private void APICALL(String token){
        try{
            if(!Utility.isNetworkAvailable(this)){
                showInfoMsg("please check internet connection");
            }else{
                Utility.showProgress(ActivityOTP.this);
                WebServiceCaller.ApiInterface login = WebServiceCaller.getClient();
                Call<LoginResponse> responseCall=login.verifyOTP(WebUtility.VERIFTY_ACTION,phone,token);
                responseCall.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        Utility.hideProgress();
                        if(response.isSuccessful()){
                            LoginResponse loginResponse=response.body();
                            if(loginResponse.isValid()){
                                showSuccessMsg(loginResponse.message.Message_);
                                if(loginResponse.userArrayList.size()>0) {
                                    appPreferences.set("USERID", loginResponse.userArrayList.get(0).UserID+"");
                                    appPreferences.set("FIRSTNAME", loginResponse.userArrayList.get(0).FirstName);
                                    appPreferences.set("LASTNAME", loginResponse.userArrayList.get(0).last_name);
                                    appPreferences.set("EMAIL", loginResponse.userArrayList.get(0).Email);
                                    appPreferences.set("PHONE", loginResponse.userArrayList.get(0).Phone);
                                    appPreferences.set("CITY", loginResponse.userArrayList.get(0).City);
                                    appPreferences.set("TOKEN", loginResponse.userArrayList.get(0).Token);
                                    appPreferences.set("IMAGE", loginResponse.userArrayList.get(0).Image);
                                    appPreferences.set("APP_NOTIFICATION", loginResponse.userArrayList.get(0).Notification);
                                    finish();
                                    startActivity(new Intent(ActivityOTP.this,Activity_Main.class));
                                }else{
                                    showSuccessMsg("No user found");
                                }

                            }else{
                                showInfoMsg(loginResponse.message.Message_);
                            }
                        }else{
                            showInfoMsg(response.message());
                        }

                    }
                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Utility.hideProgress();
                        showErrorMsg("please check internet connection");
                    }
                });

            }
        }catch (Exception ex){
            showInfoMsg(ex.toString());
        }
    }


    private void RESENDAPICALL(){
        try{
            if(!Utility.isNetworkAvailable(this)){
                showInfoMsg("please check internet connection");
            }else{
                Utility.showProgress(ActivityOTP.this);
                WebServiceCaller.ApiInterface login = WebServiceCaller.getClient();
                Call<LoginResponse> responseCall=login.login(WebUtility.LOGIN_ACTION,phone,appPreferences.getString("DEVICE_KEY"));
                responseCall.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        Utility.hideProgress();
                        if(response.isSuccessful()){
                            LoginResponse loginResponse=response.body();
                            if(loginResponse.isValid()){
                                showSuccessMsg(loginResponse.message.Message_);
                            }else{
                                showInfoMsg(loginResponse.message.Message_);
                            }
                        }else{
                            showInfoMsg(response.message());
                        }

                    }
                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Utility.hideProgress();
                        showErrorMsg("please check internet connection");
                    }
                });

            }
        }catch (Exception ex){
            showInfoMsg(ex.toString());
        }
    }
}
