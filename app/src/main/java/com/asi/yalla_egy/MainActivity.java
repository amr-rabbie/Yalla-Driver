package com.asi.yalla_egy;

import android.content.Intent;
import android.graphics.Color;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.kyleduo.switchbutton.SwitchButton;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Fragmant.EarningFragmant;
import Fragmant.HomeFrament;
import Fragmant.Notifications;
import Fragmant.ProfileFragmant;
import Fragmant.RatingsFragmant;
import LoingSession.SQLiteHandler;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;


public class  MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    SwitchButton checkOff_On;
    String NotificationNumber;
    BottomBar bottomBar;
    BottomBarTab notification;
    private ToneGenerator toneGen1;
    private JSONArray array;
    final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.getLocallang(MainActivity.this);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.Online));
        checkOff_On = (SwitchButton) findViewById(R.id.sb_offline_online);
        checkShift(checkOff_On);
        checkOff_On.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkOff_On.isChecked()) {
                    getSupportActionBar().setTitle(getResources().getString(R.string.Online));
                    changeStateRequest("IN");


                } else if (!checkOff_On.isChecked()) {
                    getSupportActionBar().setTitle(getResources().getString(R.string.offline));
                    changeStateRequest("OUT");
                }
            }
        });

        setUpBottomBar();

        Constants.SaveDriverStatus(Constants.DRIVER_STATUS_FREE, MainActivity.this);

        if (!Constants.isFirestOpen(MainActivity.this))
        {
            final SweetAlertDialog progressDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            progressDialog.getProgressHelper().setBarColor(Color.parseColor("#123afa"));
            progressDialog.setTitleText(getResources().getString(R.string.emptystring));
            progressDialog.setContentText(getResources().getString(R.string.configyourdevice));
            progressDialog.setCustomImage(R.mipmap.ic_launcher);
            progressDialog.setCancelable(false);
            progressDialog.show();


            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    Constants.setIsFirstOpen(true,MainActivity.this);
                    Intent intent = new Intent(MainActivity.this, SplachScreen.class);
                    //In API level 11 or greater, use FLAG_ACTIVITY_CLEAR_TASK and FLAG_ACTIVITY_NEW_TASK flag on Intent to clear all the activity stack.
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    System.exit(1000);
                    finish();
                }
            }, 5000);

        }
        if (savedInstanceState == null)
        {
            Fragment squadFragment = new HomeFrament();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_slide_in_from_left, R.anim.anim_slide_out_from_left);
            fragmentTransaction.replace(R.id.content_main, squadFragment, null);
            fragmentTransaction.commit();
        }


    }

    /***
     * This functions used to build bottom bar items
     */

    public void setUpBottomBar() {
        getNotificationNumbers();
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        notification = bottomBar.getTabWithId(R.id.tab_notification);

        notification.setBadgeBackgroundColor(Color.BLUE);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {
                    Log.e("ASI---->",Constants.getCompleteAddressString(MainActivity.this,30.121440, 31.367522));

                    Fragment squadFragment = new HomeFrament();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_slide_in_from_left, R.anim.anim_slide_out_from_left);
                    fragmentTransaction.replace(R.id.content_main, squadFragment, null);
                    fragmentTransaction.commit();
                } else if (tabId == R.id.tab_profile) {
                    Fragment squadFragment = new ProfileFragmant();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_slide_in_from_left, R.anim.anim_slide_out_from_left);
                    fragmentTransaction.replace(R.id.content_main, squadFragment, null);
                    fragmentTransaction.commit();
                } else if (tabId == R.id.tab_ratings) {
                    Fragment squadFragment = new RatingsFragmant();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_slide_in_from_left, R.anim.anim_slide_out_from_left);
                    fragmentTransaction.replace(R.id.content_main, squadFragment, null);
                    fragmentTransaction.commit();
                } else if (tabId == R.id.tab_earnings) {
                    Fragment squadFragment = new EarningFragmant();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_slide_in_from_left, R.anim.anim_slide_out_from_left);
                    fragmentTransaction.replace(R.id.content_main, squadFragment, null);
                    fragmentTransaction.commit();
                } else if (tabId == R.id.tab_notification) {
                    Fragment squadFragment = new Notifications();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_slide_in_from_left, R.anim.anim_slide_out_from_left);
                    fragmentTransaction.replace(R.id.content_main, squadFragment, null);
                    fragmentTransaction.commit();
                }
            }
        });
    }




    /**
     * check the shift status method
     * */
public void checkShift(SwitchButton  checkBox)
{
    if (Constants.getDriverShift(MainActivity.this).equals("0"))
    {
        checkBox.setChecked(false);
    }else {
        checkBox.setChecked(true);
    }
}

    /**
     * change driver state from IN -------- OUT
     */
    public void changeStateRequest(String InOut) {

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("DriverId", new SQLiteHandler(MainActivity.this).getUserDetails().get("uid"));
        postParam.put("ShiftStatus", InOut);
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL + "DriverShift", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {


                    //15,13
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String Flag = response.getString("Flag");
                            if (Flag.equals(Constants.DRIVER_SHIFT_OUT)) {

                                Toasty.error(MainActivity.this, getResources().getString(R.string.drivershift), Toast.LENGTH_SHORT, true).show();
                                // change shift to zero that mean driver not on the map
                                Constants.SavedDriverShift("0",MainActivity.this);
                            } else if (Flag.equals(Constants.INVALID_REQUEST)) {
                                Toasty.error(MainActivity.this, getResources().getString(R.string.thereisanerror), Toast.LENGTH_SHORT, true).show();
                            } else if (Flag.equals(Constants.DRIVER_NOT_LOGIN)) {
                            } else if (Flag.equals(Constants.USER_BLOCKED)) {
                                Toasty.error(MainActivity.this, getResources().getString(R.string.thereisanerror), Toast.LENGTH_SHORT, true).show();
                            } else if (Flag.equals(Constants.TAXI_NOT_ASSIGNED)) {
                                Toasty.error(MainActivity.this, getResources().getString(R.string.thereisanerror), Toast.LENGTH_SHORT, true).show();
                            } else if (Flag.equals(Constants.SUCCSESS)) {
                                Toasty.error(MainActivity.this, getResources().getString(R.string.thereisanerror), Toast.LENGTH_SHORT, true).show();
                            } else if (Flag.equals(Constants.DRIVER_SHIFT)) {
                                Toasty.success(MainActivity.this, getResources().getString(R.string.onlinenow), Toast.LENGTH_SHORT, true).show();
                                // change shift to ==1== that mean driver  on the map
                                Constants.SavedDriverShift("1",MainActivity.this);
                            } else if (Flag.equals(Constants.DRIVER_IN_TRIP)) {
                                Toasty.error(MainActivity.this, getResources().getString(R.string.youareintripnow), Toast.LENGTH_SHORT, true).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, "TAG");


    }


    @Override
    public void onBackPressed() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setContentText(getResources().getString(R.string.AreYou))
                .setTitleText(getResources().getString(R.string.LEAVEAPP))
                .setCancelText(getResources().getString(R.string.no))
                .setConfirmText(getResources().getString(R.string.yes))
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        finish();
                    }
                })
                .show();
    }

    public void getNotificationNumbers() {


        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.BASE_URL + "GetDriverNotification", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("RES--------> FromMAin",response);
                try {
                    JSONObject object=new JSONObject(response);
                    if (object.getString("Flag").equals("23"))
                    {

                    }else if (object.getString("Flag").equals("24")){
                         array = object.getJSONArray("driver_notifications_msg");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }




                if (array == null) {
                    NotificationNumber = "0";
                    if (NotificationNumber.equals("0")) {

                    }

                } else {
                    NotificationNumber = String.valueOf(array.length());

                    if (Integer.parseInt(NotificationNumber) > Integer.parseInt(Constants.getNotificationNumber(MainActivity.this))) {
                        int newNum = Integer.parseInt(NotificationNumber) - Integer.parseInt(Constants.getNotificationNumber(MainActivity.this));
                        notification.setBadgeCount(newNum);
                        Constants.saveNotificationNumber(NotificationNumber, MainActivity.this);

                    } else {
                       // notification.setBadgeCount(Integer.parseInt(NotificationNumber));
                    }

                }
          }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                //Log.d("ErroeVolley", error.getMessage());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                params.put("DriverId", new SQLiteHandler(MainActivity.this).getUserDetails().get("uid"));
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