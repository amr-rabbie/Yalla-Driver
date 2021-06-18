package com.asi.yalla_egy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.promotionCodeAdpter;
import Models.promotionCodeModel;

import Utile.itemclickforRecycler;

import static com.asi.yalla_egy.AppController.TripId;

public class DriverPromoCodeHistory extends AppCompatActivity {

    RecyclerView rvPay;
    promotionCodeAdpter tripsHisAdapter;
    ArrayList<promotionCodeModel> tripshistorymodels = new ArrayList<>();

    LinearLayout linearLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_promo_code_history);
        ini();
    }


    public void ini() {
        rvPay = (RecyclerView) findViewById(R.id.rvTripsHis);
        linearLoading = (LinearLayout) findViewById(R.id.loadingLayout);
        getPromotions();
        itemclickforRecycler.addTo(rvPay).setOnItemClickListener(new itemclickforRecycler.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                /**
                 * send the trip id to get complete information about it
                 * */
                Intent intent = new Intent(DriverPromoCodeHistory.this, CompletedTripDetial.class);
                intent.putExtra("tid", tripshistorymodels.get(position).getTripId());
                startActivity(intent);
            }
        });

    }

    public void getPromotions() {


        //linearLoading.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.BASE_URL + "PassengerPromoCodeHistory", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //here is the response of server


                Log.e("Data == is ", response);
                linearLoading.setVisibility(View.GONE);
                JSONArray array = null;
                try {
                    array = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (array!=null){
                    for (int n = 0; n < array.length(); n++) {
                        try {
                            JSONObject object = array.getJSONObject(n);
                            TripId = object.getString("TripId");
                            String date = object.getString("Date");
                            String Promocode = object.getString("Promocode");
                            String PassengerDiscount = object.getString("PassengerDiscount");
                            tripshistorymodels.add(new promotionCodeModel(TripId, date, Promocode, PassengerDiscount));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // do some stuff....
                    }
                    tripsHisAdapter = new promotionCodeAdpter(DriverPromoCodeHistory.this, tripshistorymodels);
                    rvPay.setLayoutManager(new LinearLayoutManager(DriverPromoCodeHistory.this, LinearLayoutManager.VERTICAL, false));
                    rvPay.setAdapter(tripsHisAdapter);
                }

            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                linearLoading.setVisibility(View.GONE);
                Log.d("ErroeVolley", error.getMessage());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                params.put("DriverId", Constants.getUserId(DriverPromoCodeHistory.this));
                return params;
            }

        };
        // Adding request to request queue
        int socketTimeout = 50000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "tag");
    }
}
