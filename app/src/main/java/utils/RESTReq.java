package utils;

import android.util.Log;

import org.json.JSONObject;

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
    private String stratoURL = "http://bayar3.eastus.cloudapp.azure.com/strato-api" ;
    private String blocServerUrl = "http://bayar3.eastus.cloudapp.azure.com/bloc/";

    private String contract_src = "contract Tracker{address public owner;uint public timestamp;uint public numberOfstates;uint public barcode;data[] public dataList;struct data{string name;uint timestamp;string note;}function TrackerCreate(uint barcode){owner=msg.sender;timestamp=now;numberOfstates=0;barcode=barcode;}function changeOwner(address retailer_account){owner=retailer_account;}function changeState(string note,string name){if(msg.sender==owner){dataList.push(data(name,now,note));numberOfstates=numberOfstates+1;}}}";
    private String create_user = "";

    OkHttpClient client = new OkHttpClient();

    public  String get_request(String url) throws IOException {

        Request request = new Request.Builder()
                .url(blocServerUrl + url)
                .build();

        Log.d("GET REQUEST: " , request.body().toString());

        Response response = client.newCall(request).execute();
        Log.d("GET REQUEST RESPONSE: " , response.body().string());
        return response.body().string();
    }

    public MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public  String post_request(String url, String json) throws IOException {
        JSONObject jsonObject = new JSONObject();


        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(blocServerUrl + url )
                .post(body)
                .build();
        Log.d("POST REQUEST: " , request.body().toString());

        Response response = client.newCall(request).execute();
        Log.d("POST REQUEST RESPONSE: " , response.body().string());

        return response.body().string();
    }
}

