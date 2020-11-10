package com.docstamp;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.docstamp.Adapter.FilesAdapter;
import com.docstamp.ApiController.FilesResponse;
import com.docstamp.Model.tblFiles;
import com.docstamp.Utils.EEditText;
import com.docstamp.Utils.TTextView;
import com.docstamp.Utils.Utility;
import com.docstamp.WebApi.WebServiceCaller;
import com.docstamp.WebApi.WebUtility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Search extends BaseActivity {

    ArrayList<tblFiles>  tblFiles=new ArrayList<>();
    FilesAdapter adapter;
    RecyclerView RvFiles;
    ImageView Menu;
    EEditText edtSearchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        RvFiles=findViewById(R.id.RvFiles);
        Menu=findViewById(R.id.Menu);
        edtSearchText=findViewById(R.id.edtSearchText);

        edtSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(!TextUtils.isEmpty(edtSearchText.getText().toString()))
                        APICALL();
                    return true;
                }
                return false;
            }
        });

            adapter=new FilesAdapter(this,tblFiles);
            RvFiles.setLayoutManager(new LinearLayoutManager(this));
            RvFiles.setAdapter(adapter);


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

    private void APICALL(){
        try{
            if(!Utility.isNetworkAvailable(this)){
                showInfoMsg("please check internet connection");
            }else{
                Utility.showProgress(Activity_Search.this);
                WebServiceCaller.ApiInterface login = WebServiceCaller.getClient();
                Call<FilesResponse> responseCall=login.search(WebUtility.SEARCH,appPreferences.getString("USERID"),edtSearchText.getText().toString().trim());
                Log.i("Request---->",responseCall.request().body().toString());
                responseCall.enqueue(new Callback<FilesResponse>() {
                    @Override
                    public void onResponse(Call<FilesResponse> call, Response<FilesResponse> response) {

                        Utility.hideProgress();
                        if(response.isSuccessful()){
                            FilesResponse loginResponse=response.body();
                            if(loginResponse.isValid()){
                                tblFiles.addAll(loginResponse.tblFiles.tblFiles);
                                tblFiles.addAll(loginResponse.tblFiles.tblGroupFiles);
                                adapter.notifyDataSetChanged();
                            }else{
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
