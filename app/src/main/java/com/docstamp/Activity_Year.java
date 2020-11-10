package com.docstamp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.docstamp.Adapter.YearAdapter;
import com.docstamp.ApiController.FolderResponse;
import com.docstamp.ApiController.YearResponse;
import com.docstamp.Model.tblYear;
import com.docstamp.Utils.TTextView;
import com.docstamp.Utils.Utility;
import com.docstamp.WebApi.WebServiceCaller;
import com.docstamp.WebApi.WebUtility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Year extends BaseActivity {

    ArrayList<tblYear>  tblYears=new ArrayList<>();
    YearAdapter adapter;
    RecyclerView RvYears;
    ImageView Menu,Search;
    TTextView tvFolderTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year);
        RvYears=findViewById(R.id.RvYears);
        tvFolderTitle=findViewById(R.id.tvFolderTitle);
        Menu=findViewById(R.id.Menu);
        Search=findViewById(R.id.Search);

//        //if(getIntent()!=null) {
//            tvFolderTitle.setText("Select Year");
////            FolderID=getIntent().getStringExtra("folderid");
////            FolderName=getIntent().getStringExtra("foldername");



       // }

        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Year.this,Activity_Search.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            tvFolderTitle.setText("Select Year");
            adapter = new YearAdapter(this, tblYears);
            RvYears.setLayoutManager(new GridLayoutManager(this, 2));
            RvYears.setAdapter(adapter);
            APICALL();
        }catch (Exception ex){
            ex.printStackTrace();
        }
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
                Utility.showProgress(Activity_Year.this);
                WebServiceCaller.ApiInterface login = WebServiceCaller.getClient();
                Call<YearResponse> responseCall=login.getAllYears(WebUtility.ALLYEARS,appPreferences.getString("USERID"));
                responseCall.enqueue(new Callback<YearResponse>() {
                    @Override
                    public void onResponse(Call<YearResponse> call, Response<YearResponse> response) {
                        Utility.hideProgress();
                        if(response.isSuccessful()){
                            YearResponse loginResponse=response.body();
                            if(loginResponse.isValid()){
                                tblYears.clear();
                                tblYears.addAll(loginResponse.tblYears);
                                adapter.notifyDataSetChanged();
                            }else{
                                showErrorMsg(loginResponse.message.Message_);
                            }
                        }else{
                            showInfoMsg(response.message());
                        }

                    }
                    @Override
                    public void onFailure(Call<YearResponse> call, Throwable t) {
                        Utility.hideProgress();
                        showErrorMsg("please contact admin");
                    }
                });

            }
        }catch (Exception ex){
            showInfoMsg(ex.toString());
        }
    }
}
