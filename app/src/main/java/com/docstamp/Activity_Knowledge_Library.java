package com.docstamp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.docstamp.Adapter.FilesAdapter;
import com.docstamp.ApiController.FilesResponse;
import com.docstamp.ApiController.KnowledgeFilesResponse;
import com.docstamp.Model.tblFiles;
import com.docstamp.Utils.TTextView;
import com.docstamp.Utils.Utility;
import com.docstamp.WebApi.WebServiceCaller;
import com.docstamp.WebApi.WebUtility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Knowledge_Library extends BaseActivity {

    ArrayList<tblFiles>  tblFiles=new ArrayList<>();
    FilesAdapter adapter;
    RecyclerView RvFiles;
    ImageView Menu,Search;
    TTextView tvFolderTitle,tvYearTitle,noDataTv;

    public static boolean is_Active=false;

    public static Activity_Knowledge_Library instance;
    public static Activity_Knowledge_Library getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);
        RvFiles=findViewById(R.id.RvFiles);
        tvFolderTitle=findViewById(R.id.FolderTitle);
        tvYearTitle=findViewById(R.id.tvYearTitle);
        Menu=findViewById(R.id.Menu);
        Search=findViewById(R.id.Search);
        noDataTv=findViewById(R.id.noDataTv);
        instance=this;

        try {
            tvYearTitle.setText("Knowledge Library");
            tvFolderTitle.setVisibility(View.INVISIBLE);
            Search.setVisibility(View.INVISIBLE);
            adapter = new FilesAdapter(this, tblFiles);
            RvFiles.setLayoutManager(new LinearLayoutManager(this));
            RvFiles.setAdapter(adapter);
            APICALL();
        }catch (Exception ex){}

        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
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

    public void APICALL(){
        try{
            if(!Utility.isNetworkAvailable(this)){
                showInfoMsg("please check internet connection");
            }else{
                Utility.showProgress(Activity_Knowledge_Library.this);
                WebServiceCaller.ApiInterface login = WebServiceCaller.getClient();
                Call<KnowledgeFilesResponse> responseCall=login.getKnowledgeFiles(WebUtility.GET_KNOWLEDGE_FILES);
                Log.i("Request---->",responseCall.request().body().toString());
                responseCall.enqueue(new Callback<KnowledgeFilesResponse>() {
                    @Override
                    public void onResponse(Call<KnowledgeFilesResponse> call, Response<KnowledgeFilesResponse> response) {

                        Utility.hideProgress();
                        if(response.isSuccessful()){
                            KnowledgeFilesResponse loginResponse=response.body();
                            if(loginResponse.isValid()){
                                tblFiles.clear();
                                tblFiles.addAll(loginResponse.filesObj);
                                adapter.notifyDataSetChanged();
                                noDataTv.setVisibility(View.GONE);
                                RvFiles.setVisibility(View.VISIBLE);
                            }else{
                                noDataTv.setVisibility(View.VISIBLE);
                                RvFiles.setVisibility(View.GONE);
                                showInfoMsg(loginResponse.message.Message_);
                            }
                        }else{
                            showInfoMsg(response.message());
                        }

                    }
                    @Override
                    public void onFailure(Call<KnowledgeFilesResponse> call, Throwable t) {
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
