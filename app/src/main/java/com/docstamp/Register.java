package com.docstamp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

public class Register extends BaseActivity implements View.OnClickListener {

    EEditText edtPhone;
    TTextView btnsend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnsend=findViewById(R.id.btnsend);
        edtPhone=findViewById(R.id.edtPhone);
        btnsend.setOnClickListener(this);
        edtPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    btnSendClick();
                    return true;
                }else
                    return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnsend:btnSendClick();break;
        }
    }
    private void btnSendClick(){
        if(TextUtils.isEmpty(edtPhone.getText().toString().trim())){
            showErrorMsg("please enter phone number");
        }
        else if(edtPhone.getText().toString().length()<10){
            showErrorMsg("please enter proper phone number");
        }else{
            APICALL();
        }
    }

    private void APICALL(){
        try{
                if(!Utility.isNetworkAvailable(this)){
                    showInfoMsg("please check internet connection");
                }else{
                    Utility.showProgress(Register.this);
                    WebServiceCaller.ApiInterface login = WebServiceCaller.getClient();
                    Call<LoginResponse> responseCall=login.login(WebUtility.LOGIN_ACTION,edtPhone.getText().toString().trim(),appPreferences.getString("DEVICE_KEY"));

                    responseCall.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            Utility.hideProgress();
                            if(response.isSuccessful()){
                                LoginResponse loginResponse=response.body();
                                if(loginResponse.isValid()){
                                    showSuccessMsg(loginResponse.message.Message_);
                                    finish();
                                    startActivity(new Intent(Register.this,ActivityOTP.class).putExtra("phone",edtPhone.getText().toString()));
                                }else{
                                    showErrorMsg(loginResponse.message.Message_);
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
