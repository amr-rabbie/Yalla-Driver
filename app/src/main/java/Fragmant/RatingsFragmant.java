package Fragmant;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asi.yalla_egy.AppController;
import com.asi.yalla_egy.Constants;
import com.asi.yalla_egy.R;
import com.asi.yalla_egy.RiderFeedBack;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Models.responseModels.rateDataForUser;

public class RatingsFragmant extends Fragment {


    CardView feedback;
    private View view;

    RatingBar ratingBar;
    TextView TotalEarningTv,CurrentRate,TvAcceptedTrips,TvRejectedTrips,tvRatingtext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


       view= inflater.inflate(R.layout.fragment_ratings_fragmant, container, false);
       ini(view);
        return view;
    }

    private void ini(View view) {
        feedback= (CardView) view.findViewById(R.id.cardFeedBack);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RiderFeedBack.class));
            }
        });
        ratingBar= (RatingBar) view.findViewById(R.id.review_ratingBar);
        TotalEarningTv = (TextView) view.findViewById(R.id.tvTotalEarning);
        CurrentRate= (TextView) view.findViewById(R.id.tvcurentrate);
        TvAcceptedTrips= (TextView) view.findViewById(R.id.tvtotaltripsaccepted);
        TvRejectedTrips= (TextView) view.findViewById(R.id.tvrejectedtrips);
        tvRatingtext= (TextView) view.findViewById(R.id.tvRatingtext);

        FeatchData();

    }
    private void FeatchData() {
        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.LOADING));
        progressDialog.show();

        final String TAG="ASI";


        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("DriverId",Constants.getUserId(getActivity()));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL+"DriverStatistics", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        rateDataForUser object=new Gson().fromJson(response.toString(),rateDataForUser.class);
                        Log.e("USER RATINGS",response.toString());
                        progressDialog.dismiss();
                        Log.d(TAG, response.toString());
                            TotalEarningTv.setText(object.getDriverTime());
                            ratingBar.setRating(Float.parseFloat(object.getTotalRate().toString()));
                            CurrentRate.setText(object.getTotalRate().toString());
                            TvAcceptedTrips.setText(object.getTotalComplete().toString());
                            TvRejectedTrips.setText(object.getTotalReject().toString());
                            tvRatingtext.setText(object.getTotalRate().toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hideProgressDialog();
                progressDialog.dismiss();
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
