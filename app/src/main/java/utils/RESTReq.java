package utils;

import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * Created by Aditya Aggarwal on 21-01-2017.
 *
 **/

public class RESTReq
{
    //singleton
    private static RESTReq request = new RESTReq();
    private RESTReq (){};
    private String stratoURL = "http://bayar3.eastus.cloudapp.azure.com/strato-api" ;
    private String blocServerUrl = "http://bayar3.eastus.cloudapp.azure.com/bloc";


    OkHttpClient client = new OkHttpClient();

    public String get_request(String url) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .build();

        Log.d("GET REQUEST: " , request.body().toString());

        Response response = client.newCall(request).execute();
        Log.d("GET REQUEST RESPONSE: " , response.body().string());
        return response.body().string();
    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public String post_request(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Log.d("POST REQUEST: " , request.body().toString());

        Response response = client.newCall(request).execute();
        Log.d("POST REQUEST RESPONSE: " , response.body().string());

        return response.body().string();
    }
}

