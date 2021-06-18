package com.asi.yalla_egy.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asi.yalla_egy.AppController;
import com.asi.yalla_egy.Constants;
import com.asi.yalla_egy.NotificationAct;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by m.khalid on 4/10/2017.
 */

public class checkNewRequest extends BroadcastReceiver
{


    @Override
    public void onReceive(Context context, Intent intent)
    {
        //context.startActivity(new Intent(context, NotificationAct.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        // put your code here
        CheckNewRequest(context);
    }
    private void CheckNewRequest( final Context context) {


        final String TAG="ASI";
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("DriverId",Constants.getUserId(context));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL+"CheckNewTripRequest", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("RES-------->",String.valueOf(response));
                        try
                        {


                            String Flag=response.getString("Flag");
                            if (Flag.equals("19"))
                            {
                                Log.d(TAG+"-->19","There is no new request ");
                            }else if(Flag.equals("1")){
                                Log.d(TAG +"-->1","INvalid request");
                            }else if(Flag.equals("20")) {
                                Log.d(TAG,"YOU HAVE NEW REQUEST");
                                String PickUpPlace=response.getString("PickUpPlace");
                                String DropPlace=response.getString("DropPlace");
                                String PassengerName=response.getString("PassengerName");
                                String TripId=response.getString("TripId");
                                String DropLatitude=response.getString("DropLatitude");
                                String DropLongitude=response.getString("DropLongitude");
                                String PickupLatitude=response.getString("PickupLatitude");
                                String PickupLongitude=response.getString("PickupLongitude");
                                String PassengerId=response.getString("PassengerId");
                                String PassengerRate=response.getString("PassengerRate");
                                String PassengerPhone=response.getString("PassengerPhone");
                                String PassengerProfileImage=response.getString("PassengerProfileImage");
                                /**
                                 * here save new trip data until it end
                                 *?
                                 * */
                                AppController.saveNewTripData(PickUpPlace,DropPlace,PassengerName,TripId,DropLatitude,DropLongitude,PickupLatitude,PickupLongitude,PassengerId,PassengerRate,PassengerPhone,PassengerProfileImage);
                                Intent dialogIntent = new Intent(context, NotificationAct.class);
                                dialogIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Constants.SaveDriverStatus(Constants.DRIVER_STATUS_GET_New_REQUEST,context);
                                context.startActivity(dialogIntent);


                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq,"TAG");
    }

}