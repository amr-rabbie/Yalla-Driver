package com.asi.yalla_egy.Services;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asi.yalla_egy.AppClasses.AppClassForDistanceCalc;
import com.asi.yalla_egy.AppController;
import com.asi.yalla_egy.Constants;
import com.asi.yalla_egy.LoginSignup;
import com.asi.yalla_egy.R;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import EventBus.Events;
import EventBus.GlobalBus;
import LoingSession.SQLiteHandler;
import LoingSession.SessionManager;

/**
 * Created by ASI on 4/7/2017.
 * this class will track Driver to get his location even app not open
 * At distance 0 meter
 * and time 10 sec
 */

public class LocationTrack extends Service
{
    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 5000;
    private static final float LOCATION_DISTANCE = 0;
    private double totaldis;
    private double dis;

    public void setTotaldis(double totaldis)
    {
        this.totaldis = totaldis;
    }

    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {
            //Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(final Location location)
        {
            //Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            /**
             * check Accuracy if only driver on trip and the speed should be more than 15 KM/H
             * */
            if (Constants.getDriverStatus(getApplicationContext()).equals((Constants.DRIVER_STATUS_IN_TRIP)))
            {

                if (location.getAccuracy() < 20 && location.getSpeed() * 3600 / 1000 > 15.0)
                {
                    locationUpdateRequest(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                }
//                else
//                {
//                    //Log.e("LOcationacc==", String.valueOf(location.getAccuracy()));
//                }

            }
            else
            {
                locationUpdateRequest(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
            }

            /**
             * here last lat long is for the end of the trip to get the address
             * */
            AppController.setLastLat(location.getLatitude());
            AppController.setLastLong(location.getLongitude());


            /**
             * at this method you will make every thing about track and speed
             * */


            if (Constants.getDriverStatus(getApplicationContext()).equals(Constants.DRIVER_STATUS_FREE))
            {


                // if driver free we will make the service component enable
                PackageManager pm = getApplicationContext().getPackageManager();
                ComponentName componentName = new ComponentName(getApplicationContext(), checkNewRequest.class);
                pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
                Intent someIntent = new Intent(getApplicationContext(), checkNewRequest.class);
                getApplicationContext().sendBroadcast(someIntent);

            }
            else if (Constants.getDriverStatus(getApplicationContext()).equals(Constants.DRIVER_STATUS_IN_TRIP) || Constants.getDriverStatus(getApplicationContext()).equals(Constants.DRIVER_STATUS_GET_New_REQUEST))
            {
                PackageManager pm = getApplicationContext().getPackageManager();
                ComponentName componentName = new ComponentName(getApplicationContext(), checkNewRequest.class);
                pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);

            }
            if (location.getAccuracy() < 20)
            {
                AppClassForDistanceCalc.setNewLAt(String.valueOf(location.getLatitude()));
                AppClassForDistanceCalc.setNewLong(String.valueOf(location.getLongitude()));
            }
//            else
//            {
//               // Log.e("LOCATION == Accuracy =>", String.valueOf(location.getAccuracy()));
//            }

        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);

        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    @Override
    public void onCreate()
    {

        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try
        {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        }
        catch (java.lang.SecurityException ex)
        {
            Log.i(TAG, "fail to request location update, ignore", ex);
        }
        catch (IllegalArgumentException ex)
        {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try
        {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        }
        catch (java.lang.SecurityException ex)
        {
            Log.i(TAG, "fail to request location update, ignore", ex);
        }
        catch (IllegalArgumentException ex)
        {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

    }

    @Override
    public void onDestroy()
    {

        super.onDestroy();
        if (mLocationManager != null)
        {
            for (int i = 0; i < mLocationListeners.length; i++)
            {
                try
                {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                }
                catch (Exception ex)
                {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }


    // initialize LocationManager
    private void initializeLocationManager()
    {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null)
        {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }


    /**
     * Location Update Track
     * @param Lat,Long
     */
    public void locationUpdateRequest(String Lat, final String Long)
    {

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("DriverId", Constants.getUserId(getApplicationContext()));
        postParam.put("Locations", Lat + "," + Long);

        // if user is Active make get the trip id from
        if (Constants.getDriverStatus(getApplicationContext()).equals("F"))
        {
            postParam.put("TripId", "0");

        }
        else
        {
            postParam.put("TripId", AppController.getTripId());
        }

        /**
         * get DriverStatus will return the value of the current driver status F --> Mean Free
         *                                                                     A--->Mean Active
         * */
        postParam.put("Status", Constants.getDriverStatus(getApplicationContext()));
        Log.e("Driver Status is -*", Constants.getDriverStatus(getApplicationContext()));
        postParam.put("TravelStatus", "0");//
        postParam.put("DeviceToken", "0");
        postParam.put("AboveMinKm", "12");
        postParam.put("DeviceType", "1");
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL + "DriverLocationUpdate", new JSONObject(postParam),
                new Response.Listener<JSONObject>()
                {


                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            String Flag = response.getString("Flag");

                            Log.e("FROM LOCATION TRACK--->", Flag);
                            if (Flag.equals("1"))
                            {

                            }
                            else if (Flag.equals("7"))
                            {

                            }
                            else if (Flag.equals("12"))
                            {
                                //String Distance = response.getString("Distance");
                                //Constants.SavedISTANCE(Distance, getApplicationContext());
                                if (Constants.getDriverStatus(getApplicationContext()).equals((Constants.DRIVER_STATUS_IN_TRIP)))
                                {
                                    if (AppClassForDistanceCalc.getOldLat() == null && AppClassForDistanceCalc.getOldLong() == null)
                                    {
                                        AppClassForDistanceCalc.setOldLat(AppClassForDistanceCalc.getNewLAt());
                                        AppClassForDistanceCalc.setOldLong(AppClassForDistanceCalc.getNewLong());
                                    }
                                    else
                                    {
                                        LatLng startLatlng = new LatLng(Double.parseDouble(AppClassForDistanceCalc.getOldLat()), Double.parseDouble(AppClassForDistanceCalc.getOldLong()));
                                        LatLng endLatlng = new LatLng(Double.parseDouble(AppClassForDistanceCalc.getNewLAt()), Double.parseDouble(AppClassForDistanceCalc.getNewLong()));
                                        //dis = Constants.CalculationByDistance(startLatlng, endLatlng);
                                        Location locationFrom = new Location("StartPoint");
                                        locationFrom.setLatitude(startLatlng.latitude);
                                        locationFrom.setLongitude(startLatlng.longitude);

                                        Location locationTo = new Location("endPoint");
                                        locationTo.setLatitude(endLatlng.latitude);
                                        locationTo.setLongitude(endLatlng.longitude);
                                        dis = locationFrom.distanceTo(locationTo);
                                        totaldis += dis / 1000;
                                        Events.UpdateTextOfDistance upDateTextKM =
                                                new Events.UpdateTextOfDistance(new DecimalFormat("##.##").format(totaldis));
                                        /**
                                         * using event bus to send data to driver while he driving
                                         * */
                                        GlobalBus.getBus().post(upDateTextKM);
                                        /**
                                         * save old dis from new dis after get the distance between the two points
                                         *
                                         * */
                                        AppClassForDistanceCalc.setOldLat(String.valueOf(AppClassForDistanceCalc.getNewLAt()));
                                        AppClassForDistanceCalc.setOldLong(String.valueOf(AppClassForDistanceCalc.getNewLong()));
                                    }
                                }
                                else
                                {

                                    Log.e("DRIVER", "Driver not in trip");
                                    totaldis = 0;
                                }
                            }
                            else if (Flag.equals("44"))
                            {
                                logoutRequest(getApplicationContext());

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
// Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    /**
     * check if user loged out from the online system the app also will make him log out automatic
     */
    private void logoutRequest(final Context context)
    {
        final String TAG = "ASI";
        Log.e("DriverId", Constants.getUserId(context));
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("DriverId", Constants.getUserId(context));
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
                                Intent intent = new Intent(context, LoginSignup.class);
                                intent.putExtra("Flag", "1");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                            else if (Flag.equals("13"))
                            {
                                Toast.makeText(context,getResources().getString(R.string.youareintripnow), Toast.LENGTH_LONG).show();
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