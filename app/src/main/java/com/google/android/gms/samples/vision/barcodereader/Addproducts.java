package com.google.android.gms.samples.vision.barcodereader;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import utils.RESTReq;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.samples.vision.barcodereader.R;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.IOException;

public class Addproducts extends Activity implements View.OnClickListener{

    private EditText product_barcode;
    private EditText product_name;
    private Button add_products;
    private Button scan_barcode;

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproducts);

        product_barcode = (EditText)findViewById(R.id.barcode_field);
        product_name = (EditText)findViewById(R.id.product_name_field);
        add_products = (Button)findViewById(R.id.add_product);
        scan_barcode = (Button)findViewById(R.id.scan_barcode);

        findViewById(R.id.add_product).setOnClickListener(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()== R.id.add_product){

              OkHttpClient client = new OkHttpClient();
//
//            MediaType mediaType = MediaType.parse("application/json");
//            Log.d("BODY", "{faucet:"+"'1'"+ ", password:"+"'123'"+"}");
//            RequestBody body = RequestBody.create(mediaType, "{faucet:"+"'1'"+ ", password:"+"'123'"+"}");
//            Request request = new Request.Builder()
//                    .url("http://bayar3.eastus.cloudapp.azure.com/bloc/users/consumers")
//                    .post(body)
//                    .addHeader("content-type", "application/json")
//                    .build();
//            try {
//                Response response = client.newCall(request).execute();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "src=contract%20Tracker%7Baddress%20public%20owner%3Buint%20public%" +
                    "20timestamp%3Buint%20public%20numberOfstates%3Buint%20public%20barcode%3Bdata%5B%5D%20public%20dataList%3B" +
                    "struct%20data%7Bstring%20name%3Buint%20timestamp%3Bstring%20note%3B%7Dfunction%20TrackerCreate(uint%20bar" +
                    "code)%7Bowner%3Dmsg.sender%3Btimestamp%3Dnow%3BnumberOfstates%3D0%3Bbarcode%3Dbarcode%3B%7Dfunction%20c" +
                    "angeOwner(address%20retailer_account)%7Bowner%3Dretailer_account%3B%7Dfunction%20changeState(string%20n" +
                    "ote%2Cstring%20name)%7Bif(msg.sender%3D%3Downer)%7BdataList.push(data(name%2Cnow%2Cnote))%3BnumberOfstate" +
                    "s%3DnumberOfstates%2B1%3B%7D%7D%7D&password=123");
            Request request = new Request.Builder()
                    .url("http://bayar3.eastus.cloudapp.azure.com/bloc/users/consumers/676fa3ec0dccdce5a3c0b87d0501e397a246c96f/contract")
                    .post(body)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .build();

            try {
                //ResponseBody responseBody = client.newCall(request).execute();

                Response response = client.newCall(request).execute();
                Log.d("Response" , response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if(view.getId()== R.id.scan_barcode){
            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    //barcodeValue.setText(barcode.displayValue);
                    product_barcode.setText(barcode.displayValue);
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                product_barcode.setText(String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
