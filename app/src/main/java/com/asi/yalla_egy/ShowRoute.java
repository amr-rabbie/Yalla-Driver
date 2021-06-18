package com.asi.yalla_egy;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asi.yalla_egy.Services.LocationTrack;
import com.asi.yalla_egy.Services.StopwatchService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import EventBus.Events;
import EventBus.GlobalBus;
import MapWork.DataParser;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import mehdi.sakout.fancybuttons.FancyButton;

import static com.asi.yalla_egy.R.id.map;


public class ShowRoute extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, View.OnClickListener {
    private GoogleMap mMap;
    ArrayList<LatLng> MarkerPoints;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    ImageView ivPassanger;
    LinearLayout tripDetail;
    FancyButton btn_drive, cancelTrip,btnCallPassenger;
    public TextView SpeedInKm, tvWattingTime, tvTotalKm, tvPassangerName;
    private PolylineOptions lineOptions;
    private ArrayList<LatLng> latLngs;

    private final long mFrequency = 100;    // milliseconds
    private final int TICK_WHAT = 2;

    private  Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            updateElapsedTime();
            sendMessageDelayed(Message.obtain(this, TICK_WHAT), mFrequency);
        }
    };
    String DriverId;
    String TripId;
    String PassengerId, passegerNumber;
    private StopwatchService m_stopwatchService;
    private ServiceConnection m_stopwatchServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            m_stopwatchService = ((StopwatchService.LocalBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            m_stopwatchService = null;
        }
    };
    private SweetAlertDialog sweetAlertDialog;
    private boolean zoomed=false;
    private float bearing;
    private float zoom=15;
    private String lastValue="";


    private void bindStopwatchService() {
        bindService(new Intent(this, StopwatchService.class),
                m_stopwatchServiceConn, Context.BIND_AUTO_CREATE);
    }

    private void unbindStopwatchService() {
        if (m_stopwatchService != null) {
            unbindService(m_stopwatchServiceConn);
        }
    }

    public void updateElapsedTime() {

//        if (m_stopwatchService != null)
//            {
//                if (m_stopwatchService.getFormattedElapsedTime() == "0") {
//                    tvWattingTime.setText(Constants.getTime(this));
//
//                }
//                else
//                {

        if (m_stopwatchService != null) {
                    tvWattingTime.setText((m_stopwatchService.getFormattedElapsedTime()));
               }


            }









    public int parseTime(String time){
        SimpleDateFormat sdf = new SimpleDateFormat(time);


        Date dt = null;
        try {
            dt = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            int hour = cal.get(Calendar.HOUR);
            int minute = cal.get(Calendar.MINUTE);
            int second = cal.get(Calendar.SECOND);
            int totalSeconds = ((hour * 60) + minute) * 60 + second;
            return totalSeconds;
        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.getLocallang(ShowRoute.this);
        setContentView(R.layout.activity_show_route);
        ini();
    }

    private void ini() {

        btn_drive =  findViewById(R.id.btn_drive);
        btn_drive.setOnClickListener(this);

        tripDetail = (LinearLayout) findViewById(R.id.currenttripdata_layout);
        SpeedInKm = (TextView) findViewById(R.id.tv_km);
        tvWattingTime = (TextView) findViewById(R.id.tv_watting_time);
        cancelTrip = (FancyButton) findViewById(R.id.btn_cancel_trip);
        cancelTrip.setVisibility(View.VISIBLE);
        cancelTrip.setOnClickListener(this);
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)        // 1 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        // Initializing
        MarkerPoints = new ArrayList<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        latLngs = new ArrayList<>();
        lineOptions = new PolylineOptions();
        startService(new Intent(this, StopwatchService.class));
        bindStopwatchService();

        tvTotalKm = (TextView) findViewById(R.id.tvTotalKm);
        DriverId = Constants.getUserId(ShowRoute.this);
        TripId = AppController.getTripId();
        PassengerId = AppController.getPassengerId();
        tvPassangerName = (TextView) findViewById(R.id.tvPassangerName);
        tvPassangerName.setText(AppController.getPassengerName());
        passegerNumber="00966"+AppController.getPassengerPhone();
        // call passenger
        btnCallPassenger= (FancyButton) findViewById(R.id.btn_call_passenger);
        btnCallPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+passegerNumber));
                startActivity(callIntent);
            }
        });

        ivPassanger= (ImageView) findViewById(R.id.ivPassanger);
//        Glide.with(ShowRoute.this).load(AppController.getPassengerProfileImage())
//                .crossFade()
//                .placeholder(R.drawable.user)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(ivPassanger);
        Picasso.with(ShowRoute.this).load(AppController.getPassengerProfileImage())
                .placeholder(R.drawable.warning)
                .error(R.drawable.warning)
                .into(ivPassanger);
//        if (ivPassanger==null)
//        {
//            ivPassanger.setImageResource(R.drawable.user);
//        }

        if (getIntent().getStringExtra("status").equals("1"))// 1 mean it open after crash from while he is in trip
        {
            cancelTrip.setVisibility(View.GONE);
            btn_drive.setText(getResources().getString(R.string.endofthetrip));
            tripDetail.setVisibility(View.VISIBLE);
            //m_stopwatchService.reset();
            startTrip();
        }else if(getIntent().getStringExtra("status").equals("2"))
        {
            btn_drive.setText(getResources().getString(R.string.startthetrip));
            btnCallPassenger.setVisibility(View.GONE);
        }
        mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), mFrequency);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_drive:

                if (btn_drive.getText().equals(getResources().getString(R.string.arriced))) {
                    btn_drive.setText(getResources().getString(R.string.startthetrip));
                    /**
                     * start the watting time
                     * */
                    driverArrived();
                } else if (btn_drive.getText().equals(getResources().getString(R.string.startthetrip))) {
                    cancelTrip.setVisibility(View.GONE);
                    btn_drive.setText(getResources().getString(R.string.endofthetrip));
                    tripDetail.setVisibility(View.VISIBLE);
                    m_stopwatchService.reset();
                    mMap.clear();
                    startTrip();
                } else if (btn_drive.getText().equals(getResources().getString(R.string.endofthetrip))) {
                    /**
                     * Here when user end the trip he will save the value as F ---> mean he now free
                     * */
                    stopService(new Intent(this, StopwatchService.class));
                    m_stopwatchService.pause();
                    tripCompleted();

                }
                break;
            case R.id.btn_cancel_trip:
                sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);

                sweetAlertDialog.setTitleText(getResources().getString(R.string.areyousure))
                        .setContentText(getResources().getString(R.string.youwanttocancelthetrip))
                        .setConfirmText(getResources().getString(R.string.yes))
                        .setCancelText(getResources().getString(R.string.no))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,ShowRoute.this);
                                CancelTrip();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sweetAlertDialog.dismiss();
                                LocationServices.FusedLocationApi.requestLocationUpdates(
                                        mGoogleApiClient, mLocationRequest, ShowRoute.this);
                            }
                        })
                        .show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Register  to listen to event.
        GlobalBus.getBus().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    // get distance from event bus
    @Subscribe
    public void getMessage(Events.UpdateTextOfDistance updateTextOfDistance) {
///////////////////////////////////
        Log.e("DIS FROM ShowRoute --->", updateTextOfDistance.getDis());
        tvTotalKm.setText(updateTextOfDistance.getDis());

    }

    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(13);
                lineOptions.color(getResources().getColor(R.color.colorPrimary));

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        mLastLocation = location;
        LatLng passanger = new LatLng(Float.parseFloat(AppController.getPickupLatitude()), Float.parseFloat(AppController.getPickupLongitude()));
        DrawLin(location,passanger);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(final Location location) {
        /**
         * Update location of the car
         * */


        // set speed on the text view
        SpeedInKm.setText(UpdateSpeed(location) + "  KM/H");
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }


        if (location.getSpeed() * 3600 / 1000 > 15.0) {

            m_stopwatchService.pause();

        } else {

            m_stopwatchService.start();


        }

        /**
         * to draw the road while user is drive the car
         * */
        if (Constants.getDriverStatus(ShowRoute.this).equals(Constants.DRIVER_STATUS_IN_TRIP)) {
            if (location.getAccuracy() < 20)
            {
                upDateLine(location);
            }

            btnCallPassenger.setVisibility(View.GONE);
        }


        latLngs.add(new LatLng(location.getLatitude(), location.getLongitude()));

       // mMap.setMyLocationEnabled(true);
        zoom=mMap.getCameraPosition().zoom;

        /**
         * check if driver cancel the trip
         * */
        if(Constants.DRIVER_STATUS_GET_New_REQUEST.equals(Constants.DRIVER_STATUS_GET_New_REQUEST))
        {
            passegerCancltheTrip();

        }
        zoom = mMap.getCameraPosition().zoom;
        AppController.setMapZoom(zoom);
        if(zoom == 2.0 || zoom <= 3.0)
        {
            AppController.setMapZoom(15f);
        }else
        {
            AppController.setMapZoom(zoom);
        }
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
        mMap.moveCamera(center);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),AppController.getMapZoom()));

    }


    /**
     * check if passenger cancel the trip
     * */
    private void passegerCancltheTrip()
    {

        final String TAG = "ASI";
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("PassengerId", AppController.getPassengerId());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL + "CheckPassengerInTrip", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            String Flag = response.getString("Flag");


                            if (Flag.equals("42")) {
                                Constants.SaveDriverStatus(Constants.DRIVER_STATUS_FREE,ShowRoute.this);
                                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,ShowRoute.this);
                                //Passenger Not In Trip
                                final SweetAlertDialog progressDialogc = new SweetAlertDialog(ShowRoute.this, SweetAlertDialog.WARNING_TYPE);

                                progressDialogc.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                progressDialogc.setTitleText(getApplicationContext().getResources().getString(R.string.passengercancelthetrip));
                                progressDialogc.setCancelable(false);
                                progressDialogc.setConfirmText(getResources().getString(R.string.done));


                                progressDialogc.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
                                    {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog)
                                        {


                                            startActivity(new Intent(ShowRoute.this,MainActivity.class));
                                           // LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,ShowRoute.this);
                                            progressDialogc.dismiss();
                                            finish();

                                        }
                                    });
                                progressDialogc.show();
//                                Toasty.error(ShowRoute.this,getApplicationContext().getResources().getString(R.string.passengercancelthetrip),Toast.LENGTH_LONG,true).show();
//                                startActivity(new Intent(ShowRoute.this,MainActivity.class));
//                                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,ShowRoute.this);
//                                finish();

                            } else if (Flag.equals("43")) {

                                //Passenger in trip

                            } else if (Flag.equals("1")) {
                                //invalid_request
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                // hideProgressDialog()

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
    protected void onDestroy() {
        super.onDestroy();
        unbindStopwatchService();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }


    /**
     * this method will draw line while driver is drive the passenger
     * @param location
     * @param dropPlace
     * */
    public void DrawLin(Location location,LatLng dropPlace) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (MarkerPoints.size() > 1) {
            MarkerPoints.clear();
            mMap.clear();
        }
        // Adding new item to the ArrayList
        Log.e("PassangerLATLNG", String.valueOf(dropPlace));
        MarkerPoints.add(latLng);
        MarkerPoints.add(dropPlace);
        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();
        MarkerOptions options2 = new MarkerOptions();
        // Setting the position of the marker
        options.position(latLng);
        options2.position(dropPlace);
        /**
         * For the start location, the color of marker is GREEN and
         * for the end location, the color of marker is RED.
         */
        if (MarkerPoints.size() == 1) {
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_taxi));
        } else if (MarkerPoints.size() == 2) {
            options2.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_placeholder));

            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_taxi));

        }
        if (bearing >= 0)
            bearing = bearing + 90;
        else
            bearing = bearing - 90;

        // Add new marker to the Google Map Android API V2
        mMap.addMarker(options.rotation(bearing).anchor(0.5f, 0.5f));

        mMap.addMarker(options2);

        // Checks, whether start and end locations are captured
        if (MarkerPoints.size() >= 2) {
            LatLng origin = MarkerPoints.get(0);
            LatLng dest = MarkerPoints.get(1);

            // Getting URL to the Google Directions API
            String url = getUrl(origin, dest);
            Log.d("onMapClick", url.toString());
            FetchUrl FetchUrl = new FetchUrl();
            // Start downloading json data from Google Directions API
            FetchUrl.execute(url);


        }

    }

    /**
     * update line while driver moved with his car
     * @param location to obtain the location of the current position
     */
    public void upDateLine(Location location) {
        LatLng dropLocation = null;
        if(AppController.getDropLatitude().equals("")&&AppController.getDropLongitude().equals(""))
        {
            dropLocation = new LatLng(0,0);

        }else {
             dropLocation = new LatLng(Float.parseFloat(AppController.getDropLatitude()), Float.parseFloat(AppController.getDropLongitude()));

        }

        DrawLin(location,dropLocation);
        LatLng passanger = new LatLng(location.getLatitude(), location.getLongitude());
        //mMap.clear();
        lineOptions.add(passanger);
        lineOptions.width(10);
        lineOptions.color(Color.RED);
        mMap.addPolyline(lineOptions);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (bearing >= 0)
            bearing = bearing + 90;
        else
            bearing = bearing - 90;
        MarkerOptions options = new MarkerOptions()
                .position(latLng).rotation(bearing)
                .title(getResources().getString(R.string.myLocation))
                .anchor(0.5f, 0.5f)
                .rotation(location.bearingTo(location));
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_taxi));
        mMap.addMarker(options);
//        mMap.setMyLocationEnabled(true);
//        CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
//        mMap.moveCamera(center);
//        if (!zoomed) {
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(14),2000, null);
//            zoomed = true;
//        }

    }

    /**
     * update the speed of the car
     * @param  location
     * */
    public String UpdateSpeed(Location location) {
        return String.valueOf(new DecimalFormat("##.#").format(location.getSpeed() * 3600 / 1000));
    }

    /**
     * while the driver arrived he send to the server that he rich the place of the passenger
     */
    public void driverArrived() {

        final String TAG = "ASI";
        Map<String, String> postParam = new HashMap<String, String>();

        postParam.put("Tripid", TripId);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL + "DriverArrived", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            String Flag = response.getString("Flag");
                            Log.e("FLAG-->", Flag);

                            if (Flag.equals("30")) {
                                //driver_arrival_send
                                Constants.SaveDriverStatus(Constants.DRIVER_STATUS_GET_New_REQUEST,ShowRoute.this);
                            } else if (Flag.equals("25")) {
                                //invalid_trip

                            } else if (Flag.equals("1")) {
                                //invalid_request
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                // hideProgressDialog();
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

    /**
     * Cancel the trip if driver need
     */
    public void CancelTrip() {

        final SweetAlertDialog progressDialog = new SweetAlertDialog(ShowRoute.this, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText(getResources().getString(R.string.LOADING));
        progressDialog.setCancelable(false);
        progressDialog.show();
        final String TAG = "ASI";
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("Tripid", TripId);
        postParam.put("Driverid", DriverId);
        postParam.put("DriverComments", "153");
        postParam.put("driverreply", "C");// C mean cancel trip
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL + "CancelTrip", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        progressDialog.dismiss();
                        try {
                            String Flag = response.getString("Flag");
                            Log.e("FLAG-->", Flag);
                            if (Flag.equals("1")) {
                                // invalid_request
                                Toast.makeText(ShowRoute.this,getResources().getString(R.string.thereisanerror), Toast.LENGTH_LONG).show();
                            } else if (Flag.equals("28")) {
                                // trip Already cancelled
                                Toast.makeText(ShowRoute.this, getResources().getString(R.string.trip_Already_cancelled), Toast.LENGTH_LONG).show();
                            } else if (Flag.equals("29")) {
                                // driver cancelled the trip

                                sweetAlertDialog.dismiss();
                                Constants.SaveDriverStatus(Constants.DRIVER_STATUS_FREE, ShowRoute.this);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                progressDialog.dismiss();
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

    /**
     * this function used when user click on start trip button
     */
    public void startTrip() {


        final String TAG = "ASI";
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("Driverid", DriverId);
        postParam.put("Tripid", TripId);
        postParam.put("Latitude", AppController.getPickupLatitude());
        postParam.put("Longitude", AppController.getPickupLongitude());
        postParam.put("status",Constants.DRIVER_IN_TRIP);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL + "TripStarted", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            String Flag = response.getString("Flag");
                            Log.e("FLAG-->+ Start the trip", Flag);

                            if (Flag.equals("32")) {
                                //trip started
                                Constants.SaveDriverStatus(Constants.DRIVER_STATUS_IN_TRIP, ShowRoute.this);
                            } else if (Flag.equals("31")) {
                                //trip cancelled by passenger

                            } else if (Flag.equals("1")) {
                                //invalid_request
                            } else if (Flag.equals("10")) {
                                //driver _not_login
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                // hideProgressDialog()

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

    /**
     * this function used when trip completed
     */
    public void tripCompleted() {
        final SweetAlertDialog progressDialog = new SweetAlertDialog(ShowRoute.this, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText(getResources().getString(R.string.LOADING));
        progressDialog.setCancelable(false);
        progressDialog.show();
        final String TAG = "ASI";
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("Droplocation", Constants.getCompleteAddressString(ShowRoute.this, AppController.getLastLat(), AppController.getLastLong()));
        postParam.put("Tripid", TripId);
        postParam.put("droplatitude", String.valueOf(AppController.getLastLat()));
        postParam.put("Droplongitude", String.valueOf(AppController.getLastLong()));
        //get the speed dis from the shared
        postParam.put("Actualdistance", tvTotalKm.getText().toString());
        postParam.put("Distance",tvTotalKm.getText().toString());
        postParam.put("Waitinghour", m_stopwatchService.getFormattedElapsedTimeM());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL + "CompleteTrip", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d(TAG, response.toString());
                        try {
                            String Flag = response.getString("Flag");

                            // Status Code :1 ,25, 34,35
                            if (Flag.equals("1")) {
                                //invalid_request
                                Toast.makeText(ShowRoute.this,getResources().getString(R.string.thereisanerror), Toast.LENGTH_LONG).show();
                            } else if (Flag.equals("25")) {
                                //invalid_trip
                                Toast.makeText(ShowRoute.this,getResources().getString(R.string.invalidtrip), Toast.LENGTH_LONG).show();
                            } else if (Flag.equals("34")) {
                                //trip_completed

                                String PassengerId = response.getString("PassengerId");
                                String Distance = response.getString("Distance");
                                String TripFare = response.getString("ActualTripFare");
                                String WaitingTime = response.getString("WaitingTime");
                                String WaitingCost = response.getString("WaitingCost");
                                String TotalFare = response.getString("TotalFare");
                                String TaxAmount = response.getString("TaxAmount");
                                String BaseFare = response.getString("BaseFare");

                                String PromotionDiscountAmount = response.getString("PromotionDiscountAmount");
                                Intent intent = new Intent(ShowRoute.this, TripCoastAndPay.class);
                                intent.putExtra("PassengerId", PassengerId);
                                intent.putExtra("DistanceSer", Distance);
                                intent.putExtra("DistanceMobile", tvTotalKm.toString());
                                intent.putExtra("ActualTripFare", TripFare);
                                intent.putExtra("WaitingTime", WaitingTime);
                                intent.putExtra("WaitingCost", WaitingCost);
                                intent.putExtra("TotalFare", TotalFare);
                                intent.putExtra("TaxAmount", TaxAmount);

                                intent.putExtra("PromotionDiscountAmount", PromotionDiscountAmount);
                                intent.putExtra("BaseFare", BaseFare);
                                Log.e("TIME FROM SERVER IS ",WaitingTime);
                                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,ShowRoute.this);
                                startActivity(intent);
                                // reset distance to zero agin
                                LocationTrack locationTrack=new LocationTrack();
                                locationTrack.setTotaldis(0);
                                finish();


                            } else if (Flag.equals("35")) {
                                //trip_already_completed
                                Toast.makeText(ShowRoute.this,getResources().getString(R.string.trip_already_completed), Toast.LENGTH_LONG).show();
                            } else if (Flag.equals("44")) {
                                //Driver not logged in
                                Toast.makeText(ShowRoute.this,getResources().getString(R.string.Driver_not_logged_in), Toast.LENGTH_LONG).show();
                            }
                            else if (Flag.equals("46")) {
                                //Invalid Trip Status
                                Toast.makeText(ShowRoute.this, getResources().getString(R.string.Invalid_Trip_Status), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                // hideProgressDialog();
                progressDialog.dismiss();
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
        if(Constants.getDriverStatus(ShowRoute.this).equals(Constants.DRIVER_STATUS_IN_TRIP))
        {
            Toasty.error(ShowRoute.this, getResources().getString(R.string.youareintripnow), Toast.LENGTH_SHORT, true).show();

        }
    }


    public void goToGoogleMap(View view)
    {
        //Uri.parse("http://maps.google.com/maps?saddr="+mLastLocation.getLatitude()+"&daddr="+mLastLocation.getLongitude()));

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr="+mLastLocation.getLatitude()+","+mLastLocation.getLongitude()));
        intent.setPackage("com.google.android.apps.maps");
        try
        {
            startActivity(intent);
        }
        catch(ActivityNotFoundException ex)
        {
            try
            {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW,  Uri.parse("http://maps.google.com/maps?saddr="+mLastLocation.getLatitude()+"&daddr="+mLastLocation.getLongitude()));
                startActivity(unrestrictedIntent);
            }
            catch(ActivityNotFoundException innerEx)
            {
                Toast.makeText(this,getResources().getString(R.string.plzinstalApp), Toast.LENGTH_LONG).show();
            }
        }

    }

}

