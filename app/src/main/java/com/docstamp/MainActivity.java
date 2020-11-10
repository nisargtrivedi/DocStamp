package com.docstamp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.docstamp.Utils.AppPreferences;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    AppPreferences appPreferences;

    private static int SPLASH_TIME_OUT = 3000;
    private static final int REQUEST_WRITE_STORAGE = 1001;
    private static final int INTERNET_REQUEST = 1002;
    private static final int READ_PHONE_REQUEST = 1003;
    private static final int CAMERA = 1004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appPreferences=new AppPreferences(this);

        if(getIntent().getStringExtra("fileid")!=null){
            Toast.makeText(MainActivity.this,getIntent().getStringExtra("fileid"),Toast.LENGTH_LONG).show();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED   && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Permission necessary");
                alertBuilder.setMessage("Write calendar permission is necessary to write event!!!");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity)MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, REQUEST_WRITE_STORAGE);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();

            } else {
                ActivityCompat.requestPermissions((Activity)MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, REQUEST_WRITE_STORAGE);
            }

            //return false;
        } else {
            //return true;
            permission();
        }

    }

    public void permission()
    {

        boolean hasPermission2 = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        boolean hasPermission3 = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        boolean hasPermission34 = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);

        if(hasPermission2 && hasPermission3 && hasPermission34) {
            new Handler().postDelayed(new Runnable() {

                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

                @Override
                public void run() {

                    if(!TextUtils.isEmpty(appPreferences.getString("USERID")+"")){
                        finish();startActivity(new Intent(MainActivity.this,Activity_Main.class));
                    }else{
//                        finish();startActivity(new Intent(MainActivity.this,Register.class));
                        finish();startActivity(new Intent(MainActivity.this,ActivityURL.class));
                    }
                }
            }, SPLASH_TIME_OUT);
        }
    }

    public File getAppDir() {
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DocStamp/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_WRITE_STORAGE:
                permission();
                break;
            case INTERNET_REQUEST:
                permission();
                break;
            case READ_PHONE_REQUEST:
                permission();
                break;
            case CAMERA:
                permission();
                break;
        }
    }
}
