package com.docstamp;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.docstamp.Utils.AppPreferences;
import com.docstamp.Utils.CircleTransform;
import com.docstamp.Utils.DownloadTask;
import com.docstamp.Utils.EEditText;
import com.docstamp.Utils.TTextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Activity_Details extends BaseActivity {

    TTextView Title,DateUpload,tvFileSize,btnshare,btnsave,msg;

    String Path="";
    ImageView Menu;
    private DownloadManager mgr=null;
    ArrayList<Long> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_details);
        Title=findViewById(R.id.Title);
        DateUpload=findViewById(R.id.DateUpload);
        tvFileSize=findViewById(R.id.tvFileSize);
        btnshare=findViewById(R.id.btnshare);
        btnsave=findViewById(R.id.btnsave);
        Menu=findViewById(R.id.Menu);
        msg=findViewById(R.id.msg);

        mgr=(DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        registerReceiver(onDownloadComplete,new IntentFilter(new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)));
        if(getIntent()!=null){
                Title.setText(getIntent().getStringExtra("file_name"));
                DateUpload.setText(getIntent().getStringExtra("date"));
                tvFileSize.setText(getIntent().getStringExtra("file_size"));
                Path=getIntent().getStringExtra("path");
        }


        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                shareBtn("Document Name :-"+Title.getText().toString()+"\n"+
//                        "Document Size :-"+tvFileSize.getText().toString()+"\n"+
//                        "Document Download Path :-"+Path+"\n"
//                );

               // new DownloadTask(Activity_Details.this, btnshare, Path,msg);
                beginDownload(Path);
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    //startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(Path)));
                    startActivity(new Intent(Activity_Details.this,Activity_ViewFile.class)
                    .putExtra("PATH",Path)
                    );
                }catch (Exception ex){
                    showInfoMsg("File Might Be Not Found please contact admin");
                }
            }
        });
        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void shareBtn(String message){
        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share Details"));
        }catch (Exception ex){
            showInfoMsg("Please Retry");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(onDownloadComplete!=null)
        unregisterReceiver(onDownloadComplete);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    private long downloadID;
    public BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            // remove it from our list
            list.remove(downloadID);

            // if list is empty means all downloads completed
            if (list.isEmpty())
            {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(Activity_Details.this)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(Title.getText().toString())
                                .setContentText("Download completed");


                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(455, mBuilder.build());
            }
            if (downloadID == id) {
                Toast.makeText(Activity_Details.this, "Download Completed", Toast.LENGTH_SHORT).show();
            }

        }
    };

    private void beginDownload(String URL){

        try {
            Uri Download_Uri = Uri.parse(URL);

            DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setAllowedOverRoaming(false);
            request.setTitle(Title.getText().toString());
            request.setDescription("Downloading " + Title.getText().toString());
            request.setVisibleInDownloadsUi(true);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, "/docstamp/"+URL.substring(URL.lastIndexOf('/') + 1));


            downloadID = mgr.enqueue(request);
            list.add(downloadID);
        }catch (Exception ex){

        }

    }
}
