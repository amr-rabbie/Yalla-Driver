package com.asi.yalla_egy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import LoingSession.SQLiteHandler;
import cn.pedant.SweetAlert.SweetAlertDialog;
import mehdi.sakout.fancybuttons.FancyButton;

public class Documents extends AppCompatActivity implements View.OnClickListener
{

    TextView tvDriverlicencesExpireDate, tvVehicleLicenseExpireDate;
    FancyButton btn_driver_license, btn_vehicle_license, btn_vehicle_insurance;
    private String VehcielInsuranceUrl;
    private String VehicleLicenseUrl;
    private String DriveLicenseUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);
        ini();
    }

    private void ini()
    {
        getDriverFiles();
        tvDriverlicencesExpireDate = (TextView) findViewById(R.id.tvDriverlicencesExpireDate);
        tvVehicleLicenseExpireDate = (TextView) findViewById(R.id.tvVehicleLicenseExpireDate);
        btn_driver_license = (FancyButton) findViewById(R.id.btn_view_driver_licenes);
        btn_vehicle_license = (FancyButton) findViewById(R.id.btn_view_vehicle_license);
        btn_vehicle_insurance = (FancyButton) findViewById(R.id.btn_view_vehicle_insurance);

        btn_driver_license.setOnClickListener(this);
        btn_vehicle_insurance.setOnClickListener(this);
        btn_vehicle_license.setOnClickListener(this);
    }


    public void getDriverFiles()
    {
        final SweetAlertDialog progressDialog = new SweetAlertDialog(Documents.this, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText(getApplicationContext().getResources().getString(R.string.LOADING));
        progressDialog.setCancelable(false);
        progressDialog.show();
        final String TAG = "ASI";
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("DriverId", new SQLiteHandler(Documents.this).getUserDetails().get("uid"));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL + "DriverFiles", new JSONObject(postParam),
                new Response.Listener<JSONObject>()
                {


                    @Override
                    public void onResponse(JSONObject response)
                    {


//                        {
//                            "IdUrl":null, "DriveLicenseUrl":null, "IdExpireDate":null, "DriveLicenseExpireDate":null, "VehicleLicenseUrl":
//                            null, "VehicleLicenseExpireDate":null, "VehcielInsuranceUrl":null, "PlateNumber":null, "Flag":2
//                        }
                        progressDialog.dismiss();
                        Log.d(TAG + "-->DriverFiles ", response.toString());
                        try
                        {
                            String Flag = response.getString("Flag");
                            if (Flag.equals("0"))
                            {
                                // request success
                                String IdUrl = response.getString("IdUrl");
                                String IdExpireDate = response.getString("IdExpireDate");
                                DriveLicenseUrl = response.getString("DriveLicenseUrl");
                                String DriveLicenseExpireDate = response.getString("DriveLicenseExpireDate");
                                tvDriverlicencesExpireDate.setText(getResources().getString(R.string.exprireDate) + DriveLicenseExpireDate);
                                VehicleLicenseUrl = response.getString("VehicleLicenseUrl");
                                String VehicleLicenseExpireDate = response.getString("VehicleLicenseExpireDate");
                                tvVehicleLicenseExpireDate.setText(getResources().getString(R.string.exprireDate) + VehicleLicenseExpireDate);
                                VehcielInsuranceUrl = response.getString("VehcielInsuranceUrl");
                                String PlateNumber = response.getString("PlateNumber");

                            }
                            else if (Flag.equals("1"))
                            {
                                // invalid request
                            }
                            else if (Flag.equals("2"))
                            {
                                // invalid user

                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.d(TAG, "Error: from cancel" + error.getMessage());
                progressDialog.dismiss();
            }
        })
        {


            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }


        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, "TAG");
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.btn_view_driver_licenes)
        {

            Intent intent=new Intent(Documents.this,ImageViewForDoc.class);
            intent.putExtra("url","https://camo.githubusercontent.com/43e80288417816260fa8907b32fd9305239494f5/687474703a2f2f6936342e74696e797069632e636f6d2f32616b39736f782e706e67");
            startActivity(intent);
        }
        else if (view.getId() == R.id.btn_view_vehicle_insurance)
        {
            Intent intent=new Intent(Documents.this,ImageViewForDoc.class);
            intent.putExtra("url","https://camo.githubusercontent.com/43e80288417816260fa8907b32fd9305239494f5/687474703a2f2f6936342e74696e797069632e636f6d2f32616b39736f782e706e67");
            startActivity(intent);
        }
        else if (view.getId() == R.id.btn_view_vehicle_license)
        {
            Intent intent=new Intent(Documents.this,ImageViewForDoc.class);
            intent.putExtra("url","https://camo.githubusercontent.com/43e80288417816260fa8907b32fd9305239494f5/687474703a2f2f6936342e74696e797069632e636f6d2f32616b39736f782e706e67");
            startActivity(intent);
        }
    }


}
