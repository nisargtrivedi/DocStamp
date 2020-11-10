package com.docstamp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.docstamp.ApiController.LoginResponse;
import com.docstamp.Utils.AppPreferences;
import com.docstamp.Utils.CircleTransform;
import com.docstamp.Utils.EEditText;
import com.docstamp.Utils.KeyBoardHandling;
import com.docstamp.Utils.TTextView;
import com.docstamp.Utils.Utility;
import com.docstamp.WebApi.WebServiceCaller;
import com.docstamp.WebApi.WebUtility;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Profile extends BaseActivity {

    com.mikhaellopez.circularimageview.CircularImageView profilepic;
    EEditText FirstName,LastName,PhoneNumber,Email;
    TTextView btnsave;
    ImageView Menu,imageUpload;

    AppPreferences appPreferences;

    private String userChoosenTask;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String imgDecodableString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        appPreferences=new AppPreferences(this);
        profilepic=findViewById(R.id.profilepic);

        FirstName=findViewById(R.id.FirstName);
        LastName=findViewById(R.id.LastName);
        PhoneNumber=findViewById(R.id.PhoneNumber);
        Email=findViewById(R.id.Email);
        Menu=findViewById(R.id.Menu);
        imageUpload=findViewById(R.id.imageUpload);
        btnsave=findViewById(R.id.btnsave);

        try {
            Glide.with(Activity_Profile.this)
                    .load(Uri.parse(appPreferences.getString("IMAGE"))) // add your image url
                    .transform(new CircleTransform(Activity_Profile.this)) // applying the image transformer
                    .into(profilepic);


            FirstName.setText(appPreferences.getString("FIRSTNAME"));
            LastName.setText(appPreferences.getString("LASTNAME"));
            PhoneNumber.setText(appPreferences.getString("PHONE"));
            Email.setText(appPreferences.getString("EMAIL"));

            Menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            imageUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage();
                }
            });
            profilepic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage();
                }
            });
            btnsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //uploadMultipart();
                    UploadImage();
                }
            });
            APICALL();
        }catch (Exception ex){
            System.out.println(ex.toString());
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void selectImage() {
        final CharSequence[] items = {"Camera Photo","Gallery Photo"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Profile.this);
        builder.setTitle("Select Option");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Camera Photo")) {
                    userChoosenTask ="Camera Photo";
                        cameraIntent();

                }
                if (items[item].equals("Gallery Photo")) {
                    userChoosenTask ="Gallery Photo";
                        galleryIntent();

                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        profilepic.setImageBitmap(null);
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        String path = saveImage(thumbnail);
        Log.i("CAMERA FILE",path);
        profilepic.setImageBitmap(thumbnail);

        imgDecodableString = path;
        appPreferences.set("profile_image",path);

    }

    private void onSelectFromGalleryResult(Intent data) {
        try {

            profilepic.setImageBitmap(null);
            Uri ImageSelect = data.getData();

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), ImageSelect);
            String path = saveImage(bitmap);

            Log.i("GALLAY FILE",path);
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(ImageSelect,filePath,null,null,null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            imgDecodableString = cursor.getString(column_index);
            appPreferences.set("profile_image",path);
            cursor.close();
            profilepic.setImageBitmap(bitmap);


        }catch (Exception e){
            Log.e("Error...",e.getMessage());
            Toast.makeText(Activity_Profile.this,"Try Again...",Toast.LENGTH_LONG).show();
        }

    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        appPreferences.set("UserImage", Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT));
        File TimeAttendance = new File(
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/DocStamp/");
        // have the object build the directory structure, if needed.
        if (!TimeAttendance.exists()) {
            TimeAttendance.mkdirs();
        }

        try {
            File f = new File(TimeAttendance, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

//    public void uploadMultipart() {
//
//        Utility.showProgress(this);
//        try {
//            String uploadId = UUID.randomUUID().toString();
//            KeyBoardHandling.hideSoftKeyboard(this);
//
//            //Creating a multi part request
//            new MultipartUploadRequest(Activity_Profile.this, uploadId, WebUtility.BASE_URL+"api.php")
//                    .setNotificationConfig(new UploadNotificationConfig())
//                    .addFileToUpload(appPreferences.getString("profile_image").isEmpty()?appPreferences.getString("IMAGE"):appPreferences.getString("profile_image"), "img") //Adding file
//                    .addParameter("action", "updateprofile")
//                    .addParameter("userid", appPreferences.getString("USERID"))
//                    .addParameter("email", Email.getText().toString().trim())
//                    .setMaxRetries(2)
//                    .setUtf8Charset()
//                    .setDelegate(new UploadStatusDelegate() {
//                        @Override
//                        public void onProgress(UploadInfo uploadInfo) {
//                            Log.i("PROGRESS", uploadInfo.getUploadRateString().toString());
//                        }
//
//                        @Override
//                        public void onError(UploadInfo uploadInfo, Exception exception) {
//                            Log.i("ERROR", uploadInfo.getElapsedTimeString().toString());
//                            Utility.hideProgress();
//
//                        }
//
//                        @Override
//                        public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
//                            Utility.hideProgress();
//                            try {
//
//                                Log.i("Response IMAGE ------>", serverResponse.getBodyAsString().toString());
//                                JSONObject object=new JSONObject(serverResponse.getBodyAsString());
//                                int error=object.getJSONObject("msg").getInt("error");
//                                String message;
//                                if(error==0) {
//                                    message = object.getJSONObject("msg").getString("message");
//                                    APICALL();
//                                }
//                                else
//                                    message=object.getJSONObject("msg").getString("message");
//
//                                showInfoMsg(message);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(UploadInfo uploadInfo) {
//                            Log.i("CANCEL", uploadInfo.toString());
//                            Utility.hideProgress();
//                        }
//                    })
//                    .startUpload(); //Starting the upload
//
//        } catch (Exception exc) {
//            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
//            Utility.hideProgress();
//        }
//    }


    private void UploadImage(){


        try{
            if(!Utility.isNetworkAvailable(this)){
                showInfoMsg("please check internet connection");
            }else {
                Utility.showProgress(Activity_Profile.this);
                WebServiceCaller.ApiInterface login = WebServiceCaller.getClient();

                //pass it like this
                File file = new File(appPreferences.getString("profile_image").isEmpty() ? appPreferences.getString("IMAGE") : appPreferences.getString("profile_image"));
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file);

// MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("img", file.getName(), requestFile);

// add another part within the multipart request
                RequestBody userid =
                        RequestBody.create(MediaType.parse("multipart/form-data"), appPreferences.getString("USERID"));

                RequestBody action =
                        RequestBody.create(MediaType.parse("multipart/form-data"), "updateprofile");

                RequestBody email =
                        RequestBody.create(MediaType.parse("multipart/form-data"), Email.getText().toString().trim());


                Call<ResponseBody> responseBodyCall = login.updateProfile(action, userid, body, email);
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        System.out.println("RESPONSE IS=====" + response.body());
                        Utility.hideProgress();
                        APICALL();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        System.out.println("RESPONSE IS=====" + t.getMessage());
                        Utility.hideProgress();
                    }
                });
            }
        }catch (Exception ex){
            System.out.println("RESPONSE IS====="+ex.getMessage());
            Utility.hideProgress();
        }
    }

    private void APICALL(){
        try{
            if(!Utility.isNetworkAvailable(this)){
                showInfoMsg("please check internet connection");
            }else{
                Utility.showProgress(Activity_Profile.this);
                WebServiceCaller.ApiInterface login = WebServiceCaller.getClient();
                Call<LoginResponse> responseCall=login.getProfile(WebUtility.GETPROFILE,appPreferences.getString("USERID"));
                responseCall.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        Utility.hideProgress();
                        if(response.isSuccessful()){
                            LoginResponse loginResponse=response.body();
                            if(loginResponse.isValid()){
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
                                    Glide.with(Activity_Profile.this)
                                            .load(Uri.parse(appPreferences.getString("IMAGE"))) // add your image url
                                            .transform(new CircleTransform(Activity_Profile.this)) // applying the image transformer
                                            .into(profilepic);
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
