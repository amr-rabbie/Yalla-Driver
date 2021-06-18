package com.asi.yalla_egy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mehdi.sakout.fancybuttons.FancyButton;

public class TripCoastAndPay extends AppCompatActivity
{

    FancyButton btn_pay,btn_pay_from_wallet;
    TextView tvTotalDistance,
            tvTripCost,
            tvWaitingTimeCost,
            tvTotalCost,
            tvStartingPoint,
            tvArrivalPosint,
            tvTaxes,
            tvTripBaseFare,
            tvTripPromotion,
            tvTripWaitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_coast_and_pay);
        ini();
    }

    private void ini()
    {
        btn_pay = (FancyButton) findViewById(R.id.btn_pay);
        // pay type 1 mean cash
        btn_pay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                tripFareUpdate("1");
            }
        });

        btn_pay_from_wallet= (FancyButton) findViewById(R.id.btn_pay_from_wallet);
        // pay type 12 mean from user wallet
        btn_pay_from_wallet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                tripFareUpdate("2");
            }
        });
        // get total distance after trip completed
        tvTotalDistance = (TextView) findViewById(R.id.tv_total_distance);
        tvTotalDistance.setText(getIntent().getStringExtra("DistanceSer"));
        // get trip cost after trip completed
        tvTripCost = (TextView) findViewById(R.id.tv_trip_cost);
        tvTripCost.setText(getIntent().getStringExtra("ActualTripFare"));
        // get waiting time time cost
        tvWaitingTimeCost = (TextView) findViewById(R.id.tv_watting_time_cost);
        tvWaitingTimeCost.setText(getIntent().getStringExtra("WaitingCost"));
        // get taxes of the trip
        tvTaxes = (TextView) findViewById(R.id.tvTaxes);
        tvTaxes.setText(getIntent().getStringExtra("taxes"));
        tvTaxes.setText(getIntent().getStringExtra("TaxAmount"));
        // get trip total cost
        tvTotalCost = (TextView) findViewById(R.id.tv_total_cost);
        tvTotalCost.setText(getIntent().getStringExtra("TotalFare"));
        // get the starting point of the trip
        tvStartingPoint = (TextView) findViewById(R.id.tvStartingPoint);
        tvStartingPoint.setText(Constants.getCompleteAddressString(TripCoastAndPay.this, AppController.getLastLat(), AppController.getLastLong()));
        // get the end point of the trip this method at Constants Class
        tvArrivalPosint = (TextView) findViewById(R.id.tvArrivalPosint);
        tvArrivalPosint.setText(Constants.getCompleteAddressString(TripCoastAndPay.this, Double.parseDouble(AppController.getPickupLatitude()), Double.parseDouble(AppController.getPickupLongitude())));
        // get base fare of the trip
        tvTripBaseFare = (TextView) findViewById(R.id.tv_trip_base_fare);
        tvTripBaseFare.setText(getIntent().getStringExtra("BaseFare"));
        // get trip promotions
        tvTripPromotion = (TextView) findViewById(R.id.tv_trip_promotion);
        tvTripPromotion.setText(getIntent().getStringExtra("PromotionDiscountAmount"));
        // get trip waiting time
        tvTripWaitTime= (TextView) findViewById(R.id.tv_trip_waiting_time);
        tvTripWaitTime.setText(getIntent().getStringExtra("WaitingTime"));
    }

    public void tripFareUpdate(String payType)
    {
        final SweetAlertDialog progressDialog = new SweetAlertDialog(TripCoastAndPay.this, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText(getApplicationContext().getResources().getString(R.string.LOADING));
        progressDialog.setCancelable(false);
        progressDialog.show();
        final String TAG = "ASI";
        Map<String, String> postParam = new HashMap<String, String>();

        postParam.put("TripId", AppController.getTripId());
        postParam.put("PaymentModId",payType);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL + "TripFareUpdate", new JSONObject(postParam),
                new Response.Listener<JSONObject>()
                {


                    @Override
                    public void onResponse(JSONObject response)
                    {

                        Log.e("DATA FROM PAY ", response.toString());
                        progressDialog.dismiss();
                        try
                        {

                            String Flag = response.getString("Flag");

                            if (Flag.equals(Constants.INVALID_REQUEST))
                            {
                                //invalid_request
                                Toast.makeText(TripCoastAndPay.this,getResources().getString(R.string.thereisanerror), Toast.LENGTH_LONG).show();
                            }
                            else if (Flag.equals("38"))
                            {
                                //trip_fare_updated

                                Intent intent = new Intent(TripCoastAndPay.this, TripCompleted.class);
                                intent.putExtra("totalcost", getIntent().getStringExtra("TotalFare"));
                                startActivity(intent);
                                finish();
                            }
                            else if (Flag.equals(Constants.INVALID_TRIP))
                            {
                                //invalid_trip
                                Toast.makeText(TripCoastAndPay.this,getResources().getString(R.string.thereisanerror), Toast.LENGTH_LONG).show();
                            }
                            else if (Flag.equals(Constants.INSUFFICIENT_WALLET_AMOUNT))
                            {
                                //no money in wallet
                                final SweetAlertDialog progressDialogc = new SweetAlertDialog(TripCoastAndPay.this, SweetAlertDialog.WARNING_TYPE);

                                progressDialogc.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                progressDialogc.setTitleText(getApplicationContext().getResources().getString(R.string.noenofmoney));
                                progressDialogc.setCancelable(false);
                                progressDialogc.setConfirmText(getResources().getString(R.string.done));


                                progressDialogc.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
                                {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog)
                                    {
                                        progressDialogc.dismiss();
                                    }
                                });
                                progressDialogc.show();
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

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                   // Toasty.error(TripCoastAndPay.this,"TimeoutError",Toast.LENGTH_LONG,true).show();
                } else if (error instanceof AuthFailureError) {
                    //Toasty.error(TripCoastAndPay.this,"AuthFailureError",Toast.LENGTH_LONG,true).show();
                } else if (error instanceof ServerError) {
                   // Toasty.error(TripCoastAndPay.this,"ServerError",Toast.LENGTH_LONG,true).show();
                } else if (error instanceof NetworkError) {
                   // Toasty.error(TripCoastAndPay.this,"NetworkError",Toast.LENGTH_LONG,true).show();
                } else if (error instanceof ParseError) {
                   // Toasty.error(TripCoastAndPay.this,"ParseError",Toast.LENGTH_LONG,true).show();
                }
                Log.d(TAG, "Error from pay " + error.getMessage());
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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, "TAG");
    }
}
