package com.docstamp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.docstamp.Adapter.FilesAdapter;
import com.docstamp.Adapter.NotificationAdapter;
import com.docstamp.ApiController.FilesResponse;
import com.docstamp.ApiController.NotificationResponse;
import com.docstamp.Model.tblFiles;
import com.docstamp.Model.tblNotification;
import com.docstamp.Utils.TTextView;
import com.docstamp.Utils.Utility;
import com.docstamp.WebApi.WebServiceCaller;
import com.docstamp.WebApi.WebUtility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Notification extends BaseActivity {

    ArrayList<tblNotification>  tblFiles=new ArrayList<>();
    NotificationAdapter adapter;
    RecyclerView RvNotifications;
    ImageView Menu,Search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        RvNotifications=findViewById(R.id.RvNotifications);
        Menu=findViewById(R.id.Menu);


        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter=new NotificationAdapter(this,tblFiles);
        RvNotifications.setLayoutManager(new LinearLayoutManager(this));
        RvNotifications.setAdapter(adapter);
        APICALL();

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void APICALL(){
        try{
            if(!Utility.isNetworkAvailable(this)){
                showInfoMsg("please check internet connection");
            }else{
                Utility.showProgress(Activity_Notification.this);
                WebServiceCaller.ApiInterface login = WebServiceCaller.getClient();
                Call<NotificationResponse> responseCall=login.getNotification(WebUtility.NOTIFICATION_LIST,appPreferences.getString("USERID"));
                Log.i("Request---->",responseCall.request().body().toString());
                responseCall.enqueue(new Callback<NotificationResponse>() {
                    @Override
                    public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {

                        Utility.hideProgress();
                        if(response.isSuccessful()){
                            NotificationResponse loginResponse=response.body();
                            if(loginResponse.isValid()){
                                tblFiles.addAll(loginResponse.tblNotifications);
                                adapter.notifyDataSetChanged();
                            }else{
                                showErrorMsg(loginResponse.message.Message_);
                            }
                        }else{
                            showInfoMsg(response.message());
                        }

                    }
                    @Override
                    public void onFailure(Call<NotificationResponse> call, Throwable t) {
                        Utility.hideProgress();
                        showErrorMsg("please check network connection");
                    }
                });

            }
        }catch (Exception ex){
            showInfoMsg(ex.toString());
        }
    }
}
