package com.docstamp;


import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.docstamp.Adapter.FolderAdapter;
import com.docstamp.Adapter.FolderAdapterList;
import com.docstamp.ApiController.FolderResponse;
import com.docstamp.ApiController.LoginResponse;
import com.docstamp.Model.tblFolder;
import com.docstamp.Utils.TTextView;
import com.docstamp.Utils.Utility;
import com.docstamp.WebApi.WebServiceCaller;
import com.docstamp.WebApi.WebUtility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Home extends HomeActivity implements View.OnClickListener {


    TTextView menuFirm,menuPersonal;
    ImageView imgGrid,imgList;
    RecyclerView RvFolders;
    ArrayList<tblFolder> tblFolders=new ArrayList<>();
    FolderAdapter adapter;
    FolderAdapterList adapterList;

    int YearID=0;
    String YearName;
    @Override
    public void setLayoutView() {
        LayoutInflater.from(this).inflate(R.layout.activity_home, lnrContainer);
        tvTitle.setText("Dashboard");

    }
    @Override
    public void initialization() {
        setDrawerState(true);
        setDrawer();
        menuFirm=findViewById(R.id.menuFirm);
        menuPersonal=findViewById(R.id.menuPersonal);
        imgGrid=findViewById(R.id.imgGrid);
        imgList=findViewById(R.id.imgList);
        RvFolders=findViewById(R.id.RvFolders);

       if(getIntent()!=null){
           YearID=getIntent().getIntExtra("yearid",0);
           YearName=getIntent().getStringExtra("yearname");
       }
        menuFirm.setOnClickListener(this);
        menuPersonal.setOnClickListener(this);
        imgGrid.setOnClickListener(this);
        imgList.setOnClickListener(this);

        adapter=new FolderAdapter(this,tblFolders,YearID+"",YearName);
        adapterList=new FolderAdapterList(this,tblFolders,YearID+"",YearName);
        RvFolders.setLayoutManager(new GridLayoutManager(this,2, LinearLayoutManager.VERTICAL,false));
        RvFolders.setAdapter(adapter);
        APICALL();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menuFirm:setDrawable(menuFirm,menuPersonal);break;
            case R.id.menuPersonal:
                //setDrawable(menuPersonal,menuFirm);
            showWarningMsg("This feature under construction");break;
            case R.id.imgGrid:
                setSrcTwo(imgGrid,imgList);
                RvFolders.setLayoutManager(new GridLayoutManager(this,2, LinearLayoutManager.VERTICAL,false));
                RvFolders.setAdapter(adapter);
                break;
            case R.id.imgList:setSrcOne(imgGrid,imgList);
                RvFolders.setLayoutManager(new LinearLayoutManager(this));
                RvFolders.setAdapter(adapterList);
                break;
        }
    }

    private void setDrawable(TTextView tv1,TTextView tv2){
            tv1.setBackgroundResource(R.drawable.tabbed);
            tv2.setBackgroundResource(R.drawable.tabbed_two);
    }
    private void setSrcOne(ImageView tv1,ImageView tv2){
        tv1.setImageResource(R.drawable.ic_gridview_inactive);
        tv2.setImageResource(R.drawable.ic_listviewactive);
    }
    private void setSrcTwo(ImageView tv1,ImageView tv2){
        tv1.setImageResource(R.drawable.ic_gridview_active);
        tv2.setImageResource(R.drawable.ic_listview_inactive);
    }

    private void APICALL(){
        try{
            if(!Utility.isNetworkAvailable(this)){
                showInfoMsg("please check internet connection");
            }else{
                Utility.showProgress(Activity_Home.this);
                WebServiceCaller.ApiInterface login = WebServiceCaller.getClient();
                final Call<FolderResponse> responseCall=login.getAllFolders(WebUtility.ALLFOLDER,YearID);
                responseCall.enqueue(new Callback<FolderResponse>() {
                    @Override
                    public void onResponse(Call<FolderResponse> call, Response<FolderResponse> response) {
                        Utility.hideProgress();
                        if(response.isSuccessful()){
                            FolderResponse loginResponse=response.body();
                            if(loginResponse.isValid()){
                                tblFolders.addAll(loginResponse.tblFolders);
                                adapter.notifyDataSetChanged();
                            }else{
                                showErrorMsg(loginResponse.message.Message_);
                            }
                        }else{
                            showInfoMsg(response.message());
                        }

                    }
                    @Override
                    public void onFailure(Call<FolderResponse> call, Throwable t) {
                        Utility.hideProgress();
                        showErrorMsg("please check your internet Connection");
                    }
                });
            }
        }catch (Exception ex){
            showInfoMsg(ex.toString());
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
