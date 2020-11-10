package com.docstamp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.docstamp.ApiController.LoginResponse;
import com.docstamp.Utils.AppPreferences;
import com.docstamp.Utils.CircleTransform;
import com.docstamp.Utils.TTextView;
import com.docstamp.Utils.Utility;
import com.docstamp.WebApi.WebServiceCaller;
import com.docstamp.WebApi.WebUtility;


import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public abstract class HomeActivity extends AppCompatActivity {

    AppPreferences appPreferences;
    ProgressDialog dialog;


   public LinearLayout lnrContainer;
   public DrawerLayout drawer_layout;
   public ImageView Menu,Search;
   public TTextView tvTitle;
   public SwitchCompat On_Off;
   public TTextView tvUsername;
   public com.mikhaellopez.circularimageview.CircularImageView imgProfile;

    private ActionBarDrawerToggle toggle;

    public abstract void setLayoutView();

    public abstract void initialization();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        lnrContainer=findViewById(R.id.lnrContainer);
        tvTitle=findViewById(R.id.tvTitle);
        drawer_layout=findViewById(R.id.drawer_layout);
        Menu=findViewById(R.id.Menu);
        Search=findViewById(R.id.Search);
        appPreferences=new AppPreferences(this);
        toggle = new ActionBarDrawerToggle(this, drawer_layout, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.setDrawerListener(toggle);
        toggle.syncState();

        setLayoutView();
        initialization();

        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.openDrawer(Gravity.LEFT);
            }
        });
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,Activity_Search.class));
            }
        });
        tvTitle.setText("Dashboard \n"+appPreferences.getString("YEAR_TITLE"));
    }
    public void setDrawerState(boolean isEnabled) {
        if (isEnabled) {
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toggle.onDrawerStateChanged(DrawerLayout.STATE_IDLE);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();

        } else {
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.onDrawerStateChanged(DrawerLayout.STATE_IDLE);
            toggle.setDrawerIndicatorEnabled(false);
            toggle.syncState();
        }
    }


    public void setDrawer() {
        //drawer.setScrimColor(Color.TRANSPARENT);
        LinearLayout llFirm = (LinearLayout) findViewById(R.id.lllFirm);
        LinearLayout lllogout=findViewById(R.id.lllogout);
        LinearLayout lllEditInfo=findViewById(R.id.lllEditInfo);
        LinearLayout lllPersonal=findViewById(R.id.lllPersonal);
        RelativeLayout llNotification=findViewById(R.id.llNotification);
        LinearLayout lllibrary=findViewById(R.id.lllibrary);
        On_Off=findViewById(R.id.On_Off);
        imgProfile=findViewById(R.id.imgMenuProfile);

        tvUsername=findViewById(R.id.tvUsername);

        tvUsername.setText(appPreferences.getString("FIRSTNAME")+" "+appPreferences.getString("LASTNAME"));
        try {
            Glide.with(HomeActivity.this)
                    .load(Uri.parse(appPreferences.getString("IMAGE"))) // add your image url
                    .transform(new CircleTransform(HomeActivity.this)) // applying the image transformer
                    .into(imgProfile);
        }catch (Exception ex){}

        if(appPreferences.getInteger("APP_NOTIFICATION",0)==0){
            On_Off.setChecked(false);
        }else{
            On_Off.setChecked(true);
        }
        On_Off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    APICALL("1");
                }else{
                    APICALL("0");
                }
            }
        });
        llNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, Activity_Notification.class);
                startActivity(i);
                drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });

        lllibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, Activity_Knowledge_Library.class);
                startActivity(i);
                drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });
        llFirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("click Home","Home Click");
                if (!(HomeActivity.this instanceof Activity_Home)) {
                    Intent i = new Intent(HomeActivity.this, Activity_Main.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(0, 0);
                }else{
                    drawer_layout.closeDrawer(Gravity.LEFT);
                }
                drawer_layout.closeDrawer(Gravity.LEFT);
            }
        });
        lllEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(HomeActivity.this,Activity_Profile.class));
                overridePendingTransition(0, 0);
            }
        });
        lllPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.closeDrawer(Gravity.LEFT);
                showInfoMsg("This feature is under development");
            }
        });
        lllogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appPreferences.set("USERID","");
                appPreferences.set("FIRSTNAME","");
                appPreferences.set("LASTNAME","");
                appPreferences.set("EMAIL","");
                appPreferences.set("PHONE","");
                appPreferences.set("CITY","");
                appPreferences.set("TOKEN","");
                appPreferences.set("profile_image","");
                appPreferences.set("URL","");
                finish();
                Intent i = new Intent(HomeActivity.this, Register.class);
                startActivity(i);
            }
        });

    }
    public void openAlert(String msg){

        Toasty.normal(this, msg, Toast.LENGTH_SHORT).show();

    }
    public void showSuccessMsg(String msg){
        Toasty.success(this, msg, Toast.LENGTH_SHORT, true).show();
    }
    public void showErrorMsg(String msg){
        Toasty.error(this, msg, Toast.LENGTH_SHORT, true).show();
    }
    public void showInfoMsg(String msg){
        Toasty.info(this, msg, Toast.LENGTH_SHORT, true).show();
    }
    public void showWarningMsg(String msg){
        Toasty.warning(this, msg, Toast.LENGTH_SHORT, true).show();
    }
    public void showNormalMsg(String msg){
        Toasty.normal(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showDialog(){
        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    public void hideDialog(){
        if(dialog.isShowing()){
            dialog.dismiss();
        }
    }


    private void APICALL(String yes_no){
        try{
            if(!Utility.isNetworkAvailable(this)){
                showInfoMsg("please check internet connection");
            }else{
                Utility.showProgress(HomeActivity.this);
                WebServiceCaller.ApiInterface login = WebServiceCaller.getClient();
                Call<LoginResponse> responseCall=login.updateNotification(WebUtility.UPDATE_NOTIFICATION,appPreferences.getString("USERID"),yes_no);
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

                                    tvUsername.setText(appPreferences.getString("FIRSTNAME")+" "+appPreferences.getString("LASTNAME"));
                                    if(appPreferences.getInteger("APP_NOTIFICATION",0)==0){
                                        On_Off.setChecked(false);
                                    }else{
                                        On_Off.setChecked(true);
                                    }

                                    Glide.with(HomeActivity.this)
                                            .load(Uri.parse(appPreferences.getString("IMAGE"))) // add your image url
                                            .transform(new CircleTransform(HomeActivity.this)) // applying the image transformer
                                            .into(imgProfile);
                                }else{
                                    showSuccessMsg("No user found");
                                }

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

