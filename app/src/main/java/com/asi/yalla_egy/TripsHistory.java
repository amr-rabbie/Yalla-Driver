package com.asi.yalla_egy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.tripsHisAdpter;
import Models.responseModels.TripHistroryResponse;
import Models.tripshistorymodel;
import Utile.itemclickforRecycler;

public class TripsHistory extends AppCompatActivity
{
    RecyclerView rvPay;
    tripsHisAdpter tripsHisAdapter;
    ArrayList<tripshistorymodel> tripshistorymodels = new ArrayList<>();

    LinearLayout linearLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Constants.getLocallang(TripsHistory.this);
        setContentView(R.layout.activity_trips_history);
        ini();
    }


    public void ini()
    {
        rvPay = (RecyclerView) findViewById(R.id.rvTripsHis);
        linearLoading = (LinearLayout) findViewById(R.id.loadingLayout);
        getTrips();

        itemclickforRecycler.addTo(rvPay).setOnItemClickListener(new itemclickforRecycler.OnItemClickListener()
        {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v)
            {

                /**
                 * send the trip id to get complete information about it
                 * */
                Intent intent = new Intent(TripsHistory.this, CompletedTripDetial.class);
                intent.putExtra("tid", tripshistorymodels.get(position).getId());
                startActivity(intent);
            }
        });

    }

    public void getTrips()
    {


        linearLoading.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.BASE_URL + "DriverCompletedTrips", new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                //here is the response of server


                linearLoading.setVisibility(View.GONE);
                JSONArray array = null;
                try
                {
                    array = new JSONArray(response);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                for (int n = 0; n < array.length(); n++)
                {
                    try
                    {
                        JSONObject object = array.getJSONObject(n);
                        TripHistroryResponse tripHistroryResponse = new Gson().fromJson(object.toString(), TripHistroryResponse.class);
                        String TripDistance = tripHistroryResponse.getTripDistance().toString();
                        String date = tripHistroryResponse.getDate();
                        String TripId = tripHistroryResponse.getTripId().toString();
                        String TripFare = tripHistroryResponse.getTripFare().toString();
                        String TripRate = tripHistroryResponse.getTripRate().toString();
                        String PassengerImage = tripHistroryResponse.getPassengerImage();
                        tripshistorymodels.add(new tripshistorymodel(TripId, date, TripDistance + " " + "KM", TripFare, TripRate, PassengerImage));
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    // do some stuff....
                }
                tripsHisAdapter = new tripsHisAdpter(TripsHistory.this, tripshistorymodels);
                rvPay.setLayoutManager(new LinearLayoutManager(TripsHistory.this, LinearLayoutManager.VERTICAL, false));
                rvPay.setAdapter(tripsHisAdapter);
            }


        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {

                linearLoading.setVisibility(View.GONE);
                Log.d("ErroeVolley", error.getMessage());

            }
        })
        {

            @Override
            protected Map<String, String> getParams()
            {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                params.put("DriverId", Constants.getUserId(TripsHistory.this));
                params.put("StartDate", "");
                params.put("Limit", "100");

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