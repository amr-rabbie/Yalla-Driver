package com.asi.yalla_egy;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TaxiProfile extends AppCompatActivity {


    ImageView taxiPic;
    TextView paletNumberOfTaxi,modelOfTaxi,colorOfTaxi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi_profile);
        ini();
    }

    private void ini() {
        paletNumberOfTaxi= (TextView) findViewById(R.id.tvPalteNumber);
        modelOfTaxi= (TextView) findViewById(R.id.tvModel);
        colorOfTaxi= (TextView) findViewById(R.id.tvColor);
        taxiPic= (ImageView) findViewById(R.id.ivTaxiImage);
        getTaxiProfile();
    }


    public void getTaxiProfile()
    {
        final SweetAlertDialog progressDialog = new SweetAlertDialog(TaxiProfile.this, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText(getApplicationContext().getResources().getString(R.string.LOADING));
        progressDialog.setCancelable(false);
        progressDialog.show();
        final String TAG = "ASI";
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("DriverId", Constants.getUserId(TaxiProfile.this));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL + "GetdTaxiProfile", new JSONObject(postParam),
                new Response.Listener<JSONObject>()
                {


                    @Override
                    public void onResponse(JSONObject response)
                    {

                        progressDialog.dismiss();
                        try
                        {

                            String Flag = response.getString("Flag");
                            if (Flag.equals("0"))
                            {
                                //Success
                                String PaletNumber=response.getString("PaletNumber");
                                String Model=response.getString("Model");
                                String VehicleImageUrl=response.getString("VehicleImageUrl");
                                String TaxiColor=response.getString("TaxiColor");
                                paletNumberOfTaxi.setText(PaletNumber);
                                modelOfTaxi.setText(Model);
                                colorOfTaxi.setText(TaxiColor);
                                Glide.with(TaxiProfile.this).load(VehicleImageUrl)
                                        .thumbnail(0.5f)
                                        .crossFade()
                                        .placeholder(R.drawable.taxi)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(taxiPic);
                                if (taxiPic==null)
                                {
                                    taxiPic.setImageResource(R.drawable.taxi);
                                }


                            }
                            else if (Flag.equals("1"))
                            {
                                // invalid request
                            }
                            else if (Flag.equals("2"))
                            {
                                // invalid user

                            }else if (Flag.equals("7"))
                            {
                                // account_deactivte

                            }
                            else if (Flag.equals("18"))
                            {
                                // taxi_not_assigned

                            }
                            else if (Flag.equals("36"))
                            {
                                // invalid_taxi

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
                Log.d(TAG, "Error" + error.getMessage());
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
}
