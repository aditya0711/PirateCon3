package com.google.android.gms.samples.vision.barcodereader;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

public class Addproducts extends Activity implements View.OnClickListener{

    private EditText product_barcode;
    private EditText product_name;
    private Button add_products;
    private Button scan_barcode;
    private Button create;
    private Button chown;
    private Button change_state;

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";
    private static final String CACHE_FILE =  "cache.tmp";
    private String contract_src = "contract Tracker{address public owner;uint public timestamp;uint public numberOfstates;uint public barcode;data[] public dataList;struct data{string name;uint timestamp;string note;}function TrackerCreate(uint barcode){owner=msg.sender;timestamp=now;numberOfstates=0;barcode=barcode;}function changeOwner(address retailer_account){owner=retailer_account;}function changeState(string note,string name){if(msg.sender==owner){dataList.push(data(name,now,note));numberOfstates=numberOfstates+1;}}}";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproducts);

        product_barcode = (EditText)findViewById(R.id.barcode_field);
        product_name = (EditText)findViewById(R.id.product_name_field);
        add_products = (Button)findViewById(R.id.add_product);
        scan_barcode = (Button)findViewById(R.id.scan_barcode2);
        change_state = (Button)findViewById(R.id.changestate);

        create = (Button)findViewById(R.id.create);
        chown = (Button)findViewById(R.id.chown);

        findViewById(R.id.add_product).setOnClickListener(this);
        findViewById(R.id.scan_barcode2).setOnClickListener(this);
        findViewById(R.id.create).setOnClickListener(this);
        findViewById(R.id.chown).setOnClickListener(this);
        findViewById(R.id.changestate).setOnClickListener(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    @Override
    public void onClick(View view) {

        if(view.getId()== R.id.add_product){
            final JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("src", "contract Tracker{address public owner;uint public timestamp;uint public numberOfstates;uint public barcode;data[] public dataList;struct data{string name;uint timestamp;string note;}function TrackerCreate(uint barcode){owner=msg.sender;timestamp=now;numberOfstates=0;barcode=barcode;}function changeOwner(address retailer_account){owner=retailer_account;}function changeState(string note,string name){if(msg.sender==owner){dataList.push(data(name,now,note));numberOfstates=numberOfstates+1;}}}");
                jsonBody.put("password", "123");
                Log.d("BODY",jsonBody.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //final String params ="\"src=contract%20Tracker%7Baddress%20public%20owner%3Buint%20public%20timestamp%3Buint%20public%20numberOfstates%3Buint%20public%20barcode%3Bdata%5B%5D%20public%20dataList%3Bstruct%20data%7Bstring%20name%3Buint%20timestamp%3Bstring%20note%3B%7Dfunction%20TrackerCreate(uint%20barcode)%7Bowner%3Dmsg.sender%3Btimestamp%3Dnow%3BnumberOfstates%3D0%3Bbarcode%3Dbarcode%3B%7Dfunction%20changeOwner(address%20retailer_account)%7Bowner%3Dretailer_account%3B%7Dfunction%20changeState(string%20note%2Cstring%20name)%7Bif(msg.sender%3D%3Downer)%7BdataList.push(data(name%2Cnow%2Cnote))%3BnumberOfstates%3DnumberOfstates%2B1%3B%7D%7D%7D&password=123\")";

            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="http://bayar3.eastus.cloudapp.azure.com/bloc/users/consumers/676fa3ec0dccdce5a3c0b87d0501e397a246c96f/contract";
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            product_name.setText(response.toString());
                            mapAddress();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            })
            {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return jsonBody.toString().getBytes("utf-8");
                        }catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jsonBody.toString(), "utf-8");
                            return null;
                            }
            }};
            queue.add(stringRequest);
        }
        else if(view.getId()== R.id.scan_barcode2){
            Log.d("INSIDE activity", "Hello");
            Intent intent = new Intent(Addproducts.this, BarcodeCaptureActivity.class);
            startActivityForResult(intent, RC_BARCODE_CAPTURE);

        }
        else if(view.getId()== R.id.changestate){
            final JSONObject jsonBody = new JSONObject();
            final JSONObject jsonin = new JSONObject();
            try {
                jsonin.put("retailer_account" , "8a69134da4570d4af0ba768def30b2aa9d7ec12d");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                jsonBody.put("password", "123");
                jsonBody.put("method","changeOwner");
                jsonBody.put("args", jsonin);
                jsonBody.put("value", 0);

                Log.d("BODY",jsonBody.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="http://bayar3.eastus.cloudapp.azure.com/bloc/users/manu/33f30f533c88b7cf871fc3aceeb52385410e3585/contract/Tracker/"+ product_name.getText()+ "/call";
            Log.d("URL" ,  url);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //product_name.setText(response.toString());
                            Log.d("Res", response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            })
            {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return jsonBody.toString().getBytes("utf-8");
                    }catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jsonBody.toString(), "utf-8");
                        return null;
                    }
                }};
            queue.add(stringRequest);
        }
        else if(view.getId()== R.id.create){
            final JSONObject jsonBody = new JSONObject();
            final JSONObject jsonin = new JSONObject();
            try {
                jsonin.put("barcode" , product_barcode.getText().toString().trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                jsonBody.put("password", "123");
                jsonBody.put("method","TrackerCreate");
                jsonBody.put("args", jsonin);
                jsonBody.put("value", 0);

                Log.d("BODY",jsonBody.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="http://bayar3.eastus.cloudapp.azure.com/bloc/users/manu/33f30f533c88b7cf871fc3aceeb52385410e3585/contract/Tracker/"+ product_name.getText()+ "/call";
            Log.d("URL" ,  url);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //product_name.setText(response.toString());
                            Log.d("Res", response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            })
            {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return jsonBody.toString().getBytes("utf-8");
                    }catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jsonBody.toString(), "utf-8");
                        return null;
                    }
                }};
            queue.add(stringRequest);
        }
        else if(view.getId() == R.id.chown){
            final JSONObject jsonBody = new JSONObject();
            final JSONObject jsonin = new JSONObject();
            try {
                jsonin.put("note" , "recd");
                jsonin.put("name" , "consumer");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                jsonBody.put("password", "123");
                jsonBody.put("method","changeState");
                jsonBody.put("args", jsonin);
                jsonBody.put("value", 0);

                Log.d("BODY",jsonBody.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="http://bayar3.eastus.cloudapp.azure.com/bloc/users/manu/33f30f533c88b7cf871fc3aceeb52385410e3585/contract/Tracker/"+ product_name.getText()+ "/call";
            Log.d("URL" ,  url);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //product_name.setText(response.toString());
                            Log.d("Res", response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            })
            {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return jsonBody.toString().getBytes("utf-8");
                    }catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jsonBody.toString(), "utf-8");
                        return null;
                    }
                }};
            queue.add(stringRequest);
        }
    }
    public void mapAddress(){
        final JSONObject jsonBody = new JSONObject();
        final JSONObject jsonin = new JSONObject();
        try {
            jsonin.put("addr" , product_name.getText());
            jsonin.put("barcode" , product_barcode.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonBody.put("password", "123456");
            jsonBody.put("method","mapAddress");
            jsonBody.put("args", jsonin);
            jsonBody.put("value", 0);

            Log.d("BODY",jsonBody.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://bayar3.eastus.cloudapp.azure.com/bloc/users/admin/4f9f51d46db7745d88df157ebb1faec904997264/contract/superContract/d59800cb1752fcc2741494da74268791463f89b3/call";
        Log.d("URL" ,  url);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //product_name.setText(response.toString());
                        Log.d("Res", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return jsonBody.toString().getBytes("utf-8");
                }catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jsonBody.toString(), "utf-8");
                    return null;
                }
            }};
        queue.add(stringRequest);

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
