package com.asi.yalla_egy;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import mehdi.sakout.fancybuttons.FancyButton;


public class TripCompleted extends AppCompatActivity  {

    RatingBar review_ratingBar;
    public int Rate;
    private Dialog dialog;
    TextView tvTripId,tvTotalCost;

    FancyButton btn_saveToWallet;
    private double remiderMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_completed);
        ini();
    }

    private void ini() {

        tvTripId= (TextView) findViewById(R.id.tvtripid);
        tvTripId.setText(AppController.getTripId());
        tvTotalCost= (TextView) findViewById(R.id.tvtotalcost);
        tvTotalCost.setText(getIntent().getStringExtra("totalcost")+" "+"SAR");
        btn_saveToWallet= (FancyButton) findViewById(R.id.btn_saveToWallet);
        btn_saveToWallet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(TripCompleted.this);
                View promptsView = li.inflate(R.layout.inputdialoge, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        TripCompleted.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);


                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.dialog_okk),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        remiderMoney=Double.parseDouble(userInput.getText().toString().trim()) - Double.parseDouble(getIntent().getStringExtra("totalcost"));
                                        if (remiderMoney != 0)
                                        {
                                            addMoneyToWallet();
                                        }

                                    }
                                })
                        .setNegativeButton(getResources().getString(R.string.dialog_cancell),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
    }

    public void BackToMain(View view) {
        Constants.SaveDriverStatus(Constants.DRIVER_STATUS_FREE, TripCompleted.this);
        startActivity(new Intent(TripCompleted.this,MainActivity.class));
        finish();
    }

    public void RatePassanger(View view) {

        // custom dialog
         dialog = new Dialog(TripCompleted.this);
        dialog.setContentView(R.layout.customratedialoge);
        dialog.setTitle("Rate Passenger Now");
        FancyButton Btn_rate= (FancyButton) dialog.findViewById(R.id.btn_rate);
        review_ratingBar= (RatingBar) dialog.findViewById(R.id.review_ratingBar);
        review_ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Rate= (int) v;

            }
        });
        dialog.show();
        Btn_rate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ratePassenger(String.valueOf(Rate));
            }
        });}

    public void ratePassenger(String passengerRate)
    {
        final SweetAlertDialog progressDialog = new SweetAlertDialog(TripCompleted.this, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText(getApplicationContext().getResources().getString(R.string.LOADING));
        progressDialog.setCancelable(false);
        progressDialog.show();

        final String TAG="ASI";
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("TripId",AppController.getTripId());
        postParam.put("PassengerId",AppController.getPassengerId());
        postParam.put("Rating",passengerRate);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL+"PassengerRating", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d(TAG, response.toString());
                        try {

                            String Flag=response.getString("Flag");
                            Log.e("FLAG-->",Flag);
                            if(Flag.equals("1"))
                            {
                                //invalid_request
                                Toast.makeText(TripCompleted.this,getResources().getString(R.string.thereisanerror),Toast.LENGTH_LONG).show();
                                startActivity(new Intent(TripCompleted.this,MainActivity.class));
                                finish();
                            }else if(Flag.equals("41"))
                            {
                                //ratting done
                                Toasty.success(TripCompleted.this,getResources().getString(R.string.passengerratedone),Toast.LENGTH_LONG,true).show();
                                dialog.dismiss();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hideProgressDialog();
            }
        }) {



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
        AppController.getInstance().addToRequestQueue(jsonObjReq,"TAG");
    }


    public void addMoneyToWallet()
    {
        final SweetAlertDialog progressDialog = new SweetAlertDialog(TripCompleted.this, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText(getApplicationContext().getResources().getString(R.string.LOADING));
        progressDialog.setCancelable(false);
        progressDialog.show();

        final String TAG="ASI";
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("WalletAmount",String.valueOf(remiderMoney));
        postParam.put("PassengerId",AppController.getPassengerId());



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL+"AddToWallet", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d(TAG, response.toString());
                        try {

                            String Flag=response.getString("Flag");
                            Log.e("FLAG-->",Flag);
                            if(Flag.equals(Constants.INVALID_REQUEST))
                            {
                                //invalid_request
                                Toasty.error(TripCompleted.this,getResources().getString(R.string.thereisanerror),Toast.LENGTH_LONG,true).show();

                            }else if(Flag.equals(Constants.AMOUNT_ADD_SUCESSFULLY))
                            {
                                //done
                                Toasty.success(TripCompleted.this,getResources().getString(R.string.amountaddedsccessfully),Toast.LENGTH_LONG,true).show();
                                progressDialog.dismiss();
                                Log.e("REMIDER MONEY===>",String.valueOf(remiderMoney));

                            }else if(Flag.equals(Constants.INVALID_USER))
                            {
                                //invalid_user
                                Toasty.error(TripCompleted.this,getResources().getString(R.string.thereisanerror),Toast.LENGTH_LONG,true).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hideProgressDialog();
            }
        }) {



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
        AppController.getInstance().addToRequestQueue(jsonObjReq,"TAG");
    }

}
