package com.docstamp.WebApi;

import android.database.Observable;
import android.util.Log;

import com.docstamp.ApiController.ClientUrlResponse;
import com.docstamp.ApiController.FilesResponse;
import com.docstamp.ApiController.FolderResponse;
import com.docstamp.ApiController.KnowledgeFilesResponse;
import com.docstamp.ApiController.LoginResponse;
import com.docstamp.ApiController.NotificationResponse;
import com.docstamp.ApiController.YearResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by nyla on 05/12/16.
 */

public class WebServiceCaller {
    private static ApiInterface ApiInterface,clientURLInterface;

    public static ApiInterface getClient() {

        if (ApiInterface == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder()
                            //.addHeader("Authorization", "Bearer "+BaseActivity.getInstance().txtToken.getText().toString())
                            //.addHeader("App-Key", "nE28E~]EeP.a")
                            .build();
                    return chain.proceed(newRequest);
                }
            }).build();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(WebUtility.BASE_URL)
                    .client(okHttpClient.newBuilder()
                            .connectTimeout(1000, TimeUnit.SECONDS)
                            .readTimeout(1000, TimeUnit.SECONDS)
                            .writeTimeout(1000, TimeUnit.SECONDS)
                            .addInterceptor(logging).build())
                    .addConverterFactory(GsonConverterFactory.create(gson)).build();
            Log.i("URL===",WebUtility.BASE_URL+"---------");
            ApiInterface = client.create(ApiInterface.class);
        }
        return ApiInterface;
    }

    public static ApiInterface getClientURL() {

        if (clientURLInterface == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder()
                            //.addHeader("Authorization", "Bearer "+BaseActivity.getInstance().txtToken.getText().toString())
                            //.addHeader("App-Key", "nE28E~]EeP.a")
                            .build();
                    return chain.proceed(newRequest);
                }
            }).build();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(WebUtility.ADMIN_URL)
                    .client(okHttpClient.newBuilder()
                            .connectTimeout(1000, TimeUnit.SECONDS)
                            .readTimeout(1000, TimeUnit.SECONDS)
                            .writeTimeout(1000, TimeUnit.SECONDS)
                            .addInterceptor(logging).build())
                    .addConverterFactory(GsonConverterFactory.create(gson)).build();
            Log.i("URL===",WebUtility.ADMIN_URL+"---------");
            clientURLInterface = client.create(ApiInterface.class);
        }
        return clientURLInterface;
    }


    public interface ApiInterface {

//        @GET("runroute/{driverId}")
//        Call<JsonElement> getRoutes(@Path("driverId") int driverId);
//
//
//
//
//        @POST("markcomplete/{runNumber}/{stopNumber}/{initials}")
//        Call<JsonElement> markDeliveryComplete(@Path("runNumber") String runNumber,
//                                               @Path("stopNumber") int stopNumber,
//                                               @Path("initials") String initials,
//                                               @Body RequestBody acceptedids);





        @FormUrlEncoded
        @POST("api.php")
        Call<LoginResponse> login(@Field("action") String action, @Field("phone") String PhoneNumber,@Field("token") String Token);

        @FormUrlEncoded
        @POST("api.php")
        Call<LoginResponse> verifyOTP(@Field("action") String action, @Field("phone") String PhoneNumber,@Field("otp") String OTP);


        @FormUrlEncoded
        @POST("api.php")
        Call<LoginResponse> getProfile(@Field("action") String action, @Field("userid") String USERID);

        @FormUrlEncoded
        @POST("api.php")
        Call<FolderResponse> getAllFolders(@Field("action") String action, @Field("yearid") int YearID);

        @FormUrlEncoded
        @POST("api.php")
        Call<YearResponse> getAllYears(@Field("action") String action, @Field("userid") String UserID);

        @FormUrlEncoded
        @POST("api.php")
        Call<FilesResponse> getAllFilesBasedOnFolder(@Field("action") String action, @Field("userid") String UserID,@Field("folderid") String FolderID,@Field("yearid") String YearID);

        @FormUrlEncoded
        @POST("api.php")
        Call<FilesResponse> search(@Field("action") String action, @Field("userid") String UserID,@Field("search_keyword") String SearchText);

        @FormUrlEncoded
        @POST("api.php")
        Call<LoginResponse> updateNotification(@Field("action") String action, @Field("userid") String UserID,@Field("notification") String Notification);

        @FormUrlEncoded
        @POST("api.php")
        Call<NotificationResponse> getNotification(@Field("action") String action, @Field("userid") String UserID);


        @FormUrlEncoded
        @POST("api.php")
        Call<KnowledgeFilesResponse> getKnowledgeFiles(@Field("action") String action);

        @FormUrlEncoded
        @POST("api.php")
        Call<ClientUrlResponse> getClientURl(@Field("action") String action,@Field("username") String username);


        @Multipart
        @POST("api.php")
        Call<ResponseBody> updateProfile(@Part("action") RequestBody action,
                                               @Part("userid") RequestBody userid,
                                               @Part MultipartBody.Part image,
                                               @Part("email") RequestBody email);


    }
}
