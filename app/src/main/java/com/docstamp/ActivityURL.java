package com.docstamp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.docstamp.ApiController.ClientUrlResponse;
import com.docstamp.ApiController.LoginResponse;
import com.docstamp.Utils.EEditText;
import com.docstamp.Utils.TTextView;
import com.docstamp.Utils.Utility;
import com.docstamp.WebApi.WebServiceCaller;
import com.docstamp.WebApi.WebUtility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityURL extends BaseActivity {

    EEditText edtURL;
    TTextView btnsend;
    CheckBox chkTerms;
    TextView tvTerms;
    String URL="https://docstamp.in/terms_and_condition.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url);
        btnsend=findViewById(R.id.btnsend);
        edtURL=findViewById(R.id.edtURL);
        chkTerms=findViewById(R.id.chkTerms);
        tvTerms=findViewById(R.id.tvTerms);

        if(!TextUtils.isEmpty(appPreferences.getString("URL"))){
            WebUtility.BASE_URL = appPreferences.getString("URL");
            if(!TextUtils.isEmpty(appPreferences.getString("USERID")+"")) {
                finish();
                startActivity(new Intent(ActivityURL.this, Activity_Main.class));
            }
        }
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chkTerms.isChecked()) {
                    if(!TextUtils.isEmpty(edtURL.getText().toString())) {
                        APICALL();
                    }else{
                        showErrorMsg("Please enter URL");

                    }
                }else{
                    showErrorMsg("Please agree terms and condition");
                }
            }
        });

        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                    startActivity(intent);
                }catch (Exception ex){

                }
            }
        });
    }

    private void APICALL(){
        try{
            if(!Utility.isNetworkAvailable(this)){
                showInfoMsg("please check internet connection");
            }else{
                Utility.showProgress(ActivityURL.this);
                WebServiceCaller.ApiInterface login = WebServiceCaller.getClientURL();
                Call<ClientUrlResponse> responseCall=login.getClientURl(WebUtility.GET_URL,edtURL.getText().toString().trim());

                responseCall.enqueue(new Callback<ClientUrlResponse>() {
                    @Override
                    public void onResponse(Call<ClientUrlResponse> call, Response<ClientUrlResponse> response) {
                        Utility.hideProgress();
                        if(response.isSuccessful()){
                            ClientUrlResponse loginResponse=response.body();
                            if(loginResponse.isValid()){
                                appPreferences.set("URL", loginResponse.clients.ClientURL);
                                //System.out.println("URL==>"+loginResponse.clients.ClientURL);
                                WebUtility.BASE_URL = appPreferences.getString("URL");
                                finish();
                                startActivity(new Intent(ActivityURL.this, Register.class));

                            }else{
                                showErrorMsg(loginResponse.message.Message_);
                            }
                        }else{
                            showInfoMsg(response.message());
                        }

                    }
                    @Override
                    public void onFailure(Call<ClientUrlResponse> call, Throwable t) {
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
