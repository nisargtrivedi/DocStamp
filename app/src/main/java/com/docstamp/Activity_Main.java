package com.docstamp;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.docstamp.Adapter.FirmAdapter;
import com.docstamp.Adapter.FirmAdapterList;
import com.docstamp.Adapter.FolderAdapter;
import com.docstamp.Adapter.FolderAdapterList;
import com.docstamp.ApiController.FolderResponse;
import com.docstamp.Model.tblFirm;
import com.docstamp.Model.tblFolder;
import com.docstamp.Utils.TTextView;
import com.docstamp.Utils.Utility;
import com.docstamp.WebApi.WebServiceCaller;
import com.docstamp.WebApi.WebUtility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Main extends HomeActivity implements View.OnClickListener {


    TTextView menuFirm,menuPersonal;
    ImageView imgGrid,imgList;
    RecyclerView RvFolders;
    ArrayList<tblFirm> tblFirm=new ArrayList<>();
    FirmAdapter adapter;
    FirmAdapterList adapterList;
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

        menuFirm.setOnClickListener(this);
        menuPersonal.setOnClickListener(this);
        imgGrid.setOnClickListener(this);
        imgList.setOnClickListener(this);

        adapter=new FirmAdapter(this,tblFirm);
        adapterList=new FirmAdapterList(this,tblFirm);
        RvFolders.setLayoutManager(new GridLayoutManager(this,2, LinearLayoutManager.VERTICAL,false));
        RvFolders.setAdapter(adapter);
        setData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvTitle.setText("Home");

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

    private void setData(){
        try{
                tblFirm.add(new tblFirm(1,"IT-Tax Auditor",R.drawable.it_img));
                tblFirm.add(new tblFirm(2,"Lawyer",R.drawable.lawyer_img));
                tblFirm.add(new tblFirm(3,"Doctor",R.drawable.doctor_img));
                adapter.notifyDataSetChanged();


        }catch (Exception ex){
            showInfoMsg(ex.toString());
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
