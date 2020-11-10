package com.docstamp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.docstamp.Adapter.FilesAdapter;
import com.docstamp.Adapter.YearAdapter;
import com.docstamp.ApiController.FilesResponse;
import com.docstamp.ApiController.YearResponse;
import com.docstamp.Model.tblFiles;
import com.docstamp.Model.tblYear;
import com.docstamp.Utils.TTextView;
import com.docstamp.Utils.Utility;
import com.docstamp.WebApi.WebServiceCaller;
import com.docstamp.WebApi.WebUtility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Files extends BaseActivity {

    ArrayList<tblFiles>  tblFiles=new ArrayList<>();
    FilesAdapter adapter;
    RecyclerView RvFiles;
    ImageView Menu,Search;
    TTextView tvFolderTitle,tvYearTitle,noDataTv;

    String FolderID="0";
    String YearID="0";

    public static boolean is_Active=false;
    public static Activity_Files instance;

    public static Activity_Files getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);
        RvFiles=findViewById(R.id.RvFiles);
        tvFolderTitle=findViewById(R.id.FolderTitle);
        tvYearTitle=findViewById(R.id.tvYearTitle);
        noDataTv=findViewById(R.id.noDataTv);
        Menu=findViewById(R.id.Menu);
        Search=findViewById(R.id.Search);

        instance = this;
        if(getIntent()!=null) {
            tvFolderTitle.setText(getIntent().getStringExtra("foldername"));
            tvYearTitle.setText(getIntent().getStringExtra("yeartitle"));
            YearID=getIntent().getStringExtra("yearid");
            FolderID=getIntent().getStringExtra("folderid");

            Log.i("YEARID---->",YearID);
            Log.i("FOLDERID---->",FolderID);

            adapter=new FilesAdapter(this,tblFiles);
            RvFiles.setLayoutManager(new LinearLayoutManager(this));
            RvFiles.setAdapter(adapter);
            APICALL();
        }
        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();startActivity(new Intent(Activity_Files.this,Activity_Search.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        is_Active=true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        is_Active=false;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void APICALL(){
        try{
            if(!Utility.isNetworkAvailable(this)){
                showInfoMsg("please check internet connection");
            }else{
                Utility.showProgress(Activity_Files.this);
                WebServiceCaller.ApiInterface login = WebServiceCaller.getClient();
                Call<FilesResponse> responseCall=login.getAllFilesBasedOnFolder(WebUtility.ALLFOLDERFILES,appPreferences.getString("USERID"),FolderID,YearID);
                Log.i("Request---->",responseCall.request().body().toString());
                responseCall.enqueue(new Callback<FilesResponse>() {
                    @Override
                    public void onResponse(Call<FilesResponse> call, Response<FilesResponse> response) {

                        Utility.hideProgress();
                        if(response.isSuccessful()){
                            FilesResponse loginResponse=response.body();
                            if(loginResponse.isValid()){
                                tblFiles.clear();
                                tblFiles.addAll(loginResponse.tblFiles.tblFiles);
                                tblFiles.addAll(loginResponse.tblFiles.tblGroupFiles);
                                adapter.notifyDataSetChanged();
                                noDataTv.setVisibility(View.GONE);
                                RvFiles.setVisibility(View.VISIBLE);
                            }else{
                                noDataTv.setVisibility(View.VISIBLE);
                                RvFiles.setVisibility(View.GONE);
                                showErrorMsg(loginResponse.message.Message_);
                            }
                        }else{
                            showInfoMsg(response.message());
                        }

                    }
                    @Override
                    public void onFailure(Call<FilesResponse> call, Throwable t) {
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
