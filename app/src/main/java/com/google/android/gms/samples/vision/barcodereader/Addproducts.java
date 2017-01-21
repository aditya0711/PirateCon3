package com.google.android.gms.samples.vision.barcodereader;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.samples.vision.barcodereader.R;
import com.google.android.gms.vision.barcode.Barcode;

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
        findViewById(R.id.scan_barcode).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()== R.id.add_product){
            //request.get();

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
