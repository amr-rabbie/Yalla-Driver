package com.asi.yalla_egy;


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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.commentAdapter;
import Models.commentsModel;


public class RiderFeedBack extends AppCompatActivity {

    LinearLayout lodinglayout;
    RecyclerView rvComments;
    commentAdapter commentAdapter;
    ArrayList<commentsModel> commentsModels=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_feed_back);
        ini();

    }

    public void ini()
    {
        lodinglayout= (LinearLayout) findViewById(R.id.loadingLayout);
        rvComments= (RecyclerView) findViewById(R.id.rvcomments);
        //fetchDriverCommentsRequest();
        getDriverComments();
    }




    public  void getDriverComments()
    {


        lodinglayout.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.BASE_URL+"DriverCommentRate", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //here is the response of server
                lodinglayout.setVisibility(View.GONE);

                JSONArray array = null;
                try {
                    array = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int n = 0; n < array.length(); n++)
                {
                    try {
                        JSONObject object = array.getJSONObject(n);
                        String rate=object.getString("Rate");
                        String comment=object.getString("CommentStr");
                        String RateDate=object.getString("RateDate");
                        commentsModels.add(new commentsModel(comment,rate,RateDate));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // do some stuff....
                }
                commentAdapter=new commentAdapter(RiderFeedBack.this,commentsModels);
                rvComments.setAdapter(commentAdapter);
                rvComments.setLayoutManager(new LinearLayoutManager(RiderFeedBack.this,LinearLayoutManager.VERTICAL,false));
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                lodinglayout.setVisibility(View.GONE);
                Log.d("ErroeVolley",error.getMessage());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                params.put("DriverId",Constants.getUserId(RiderFeedBack.this));

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