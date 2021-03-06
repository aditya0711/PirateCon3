/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.gms.samples.vision.barcodereader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

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

import java.io.UnsupportedEncodingException;

import utils.RESTReq;


/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * reads barcodes.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    // use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView product_name;
    private TextView owner_history;
    private TextView barcodeValue;
    private EditText barcodeValueEdit;

    private Button searchProduct;
    private Button changeOwner;
    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barcodeValue = (TextView)findViewById(R.id.barcode);
        product_name = (TextView)findViewById(R.id.product_name);
        owner_history = (TextView)findViewById(R.id.own_history);

        searchProduct = (Button)findViewById(R.id.search_product);
        changeOwner = (Button)findViewById(R.id.change_owner);

        barcodeValueEdit = (EditText)findViewById(R.id.editText3);

        autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);

        findViewById(R.id.read_barcode).setOnClickListener(this);
        findViewById(R.id.change_owner).setOnClickListener(this);
        findViewById(R.id.search_product).setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Addproducts.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        RESTReq request;
        //request.get_request()

        if (v.getId() == R.id.read_barcode) {
            // launch barcode activity.
            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        }
        else if(v.getId()== R.id.search_product){
            final JSONObject jsonBody = new JSONObject();
            final JSONObject jsonin = new JSONObject();
            try {
                jsonin.put("barcode" , barcodeValueEdit.getText().toString().trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                jsonBody.put("password", "123456");
                jsonBody.put("method","getAddress");
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
                            Log.d("Responseeeeeeeeeee", response);
                            String contract_address = response.toString().trim().substring(22);
                            Log.d("Res con id", contract_address);
                            mapAddress(contract_address);
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

        }else if(v.getId()== R.id.change_owner){
            //request.post();
        }
    }
    public void mapAddress(String contractID){

        RequestQueue queue = Volley.newRequestQueue(this);
        //String url ="http://bayar3.eastus.cloudapp.azure.com/bloc/contracts/superContract/d59800cb1752fcc2741494da74268791463f89b3/state";
        String url ="http://bayar3.eastus.cloudapp.azure.com/bloc/contracts/Tracker/"+contractID+ "/state";
        Log.d("URL" ,  url);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //product_name.setText(response.toString());
                        Log.d("ResList", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            owner_history.setText(obj.get("dataList").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);

    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    //barcodeValue.setText(barcode.displayValue);
                    barcodeValueEdit.setText(barcode.displayValue);
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    //statusMessage.setText(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
