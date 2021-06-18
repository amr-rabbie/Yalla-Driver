package com.asi.yalla_egy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import es.dmoral.toasty.Toasty;

public class ProfitHistory extends AppCompatActivity
{
    RecyclerView rvPay;
    promotionCodeAdpter tripsHisAdapter;
    ArrayList<promotionCodeModel> tripshistorymodels = new ArrayList<>();

    LinearLayout linearLoading;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit_history);

        rvPay = (RecyclerView) findViewById(R.id.rvTripsHis);
        linearLoading = (LinearLayout) findViewById(R.id.loadingLayout);

        getProfitHsis(getIntent().getStringExtra("year"),getIntent().getStringExtra("month"));
    }

    public void getProfitHsis(final String year, final String month) {


        //linearLoading.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.BASE_URL + "DriverEarningHistory", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //here is the response of server

                JSONArray array = null;
                Log.e("Data == is ", response);
                linearLoading.setVisibility(View.GONE);

                try {
                    JSONObject object=new JSONObject(response);
                    if (object.getString("Flag").equals("50"))
                    {
                        Toasty.info(ProfitHistory.this,getResources().getString(R.string.No_earning_history),Toast.LENGTH_LONG,true).show();

                    }else if (object.getString("Flag").equals("0")){
                        array = object.getJSONArray("EarnHistory");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                try {
                    array = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (array != null)
                {
                    for (int n = 0; n < array.length(); n++) {
                        try {
                            JSONObject object = array.getJSONObject(n);

                            String date = object.getString("Date");
                            String Earning = object.getString("Earning");

                            tripshistorymodels.add(new promotionCodeModel("", date, Earning, ""));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // do some stuff....
                    }
                }

                tripsHisAdapter = new promotionCodeAdpter(ProfitHistory.this, tripshistorymodels);
                rvPay.setLayoutManager(new LinearLayoutManager(ProfitHistory.this, LinearLayoutManager.VERTICAL, false));
                rvPay.setAdapter(tripsHisAdapter);
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                linearLoading.setVisibility(View.GONE);


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                params.put("DriverId", Constants.getUserId(ProfitHistory.this));
                params.put("month",month);
                params.put("year",year);
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
