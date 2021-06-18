package com.asi.yalla_egy.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asi.yalla_egy.AppController;
import com.asi.yalla_egy.Constants;
import com.asi.yalla_egy.LoginSignup;
import com.asi.yalla_egy.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import LoingSession.SQLiteHandler;
import LoingSession.SessionManager;

/**
 * Created by m.khalid on 5/9/2017.
 */

public class LogoutBreoadCastReciver extends BroadcastReceiver
{


    @Override
    public void onReceive(Context context, Intent intent)
    {
        //context.startActivity(new Intent(context, NotificationAct.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        // put your code here
        logoutRequest(context);

    }
    private void logoutRequest(final Context context)
    {



        final String TAG = "ASI";
        Log.e("DriverId", Constants.getUserId(context));
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("DriverId",Constants.getUserId(context));
        postParam.put("ShiftId", new SQLiteHandler(context).getUserDetails().get("shift_id"));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL + "DriverLogout", new JSONObject(postParam),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {


                        Log.d(TAG, response.toString());
                        try
                        {
                            String Flag = response.getString("Flag");
                            Log.e("FLAG-->", Flag);
                            if (Flag.equals("14"))
                            {
                                new SessionManager(context).setLogin(false);
                                new SQLiteHandler(context).deleteUsers();
                                Intent intent=new Intent();
                                intent.putExtra("Flag","1");
                                intent.setClass(context, LoginSignup.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(intent);


                            }else if(Flag.equals("13"))
                            {

                                Toast.makeText(context,context.getResources().getString(R.string.youareintripnow),Toast.LENGTH_LONG).show();
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
                Log.d(TAG, "Error: " + error.getMessage());
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
