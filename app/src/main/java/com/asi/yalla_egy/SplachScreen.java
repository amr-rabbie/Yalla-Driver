package com.asi.yalla_egy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asi.yalla_egy.Services.LocationTrack;
import com.daimajia.androidanimations.library.Techniques;
import com.google.gson.Gson;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import LoingSession.SessionManager;
import Models.responseModels.DriverPendingBooking;
import es.dmoral.toasty.Toasty;

public class SplachScreen extends AwesomeSplash
{


    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CALL_PHONE
    };

    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    @Override
    public void initSplash(ConfigSplash configSplash)
    {
            /* you don't have to override every property */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.Wheat); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(1000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.loooooosogooo); //or any other drawable
        configSplash.setAnimLogoSplashDuration(1000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeInLeft); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        //Customize Title
        configSplash.setTitleSplash("YALLA-DRIVER");
        configSplash.setTitleTextColor(R.color.colorPrimary);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(2000);
        configSplash.setAnimTitleTechnique(Techniques.FadeInRight);

        /**
         * Call the service of the app
         * */
        changeLang(SplachScreen.this);
        Intent serviceIntent = new Intent(SplachScreen.this, LocationTrack.class);
        startService(serviceIntent);
    }

    @Override
    public void animationsFinished()
    {

//        if (isLocationEnabled(SplachScreen.this)){
//            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//                    .setTitleText(getResources().getString(R.string.locationnotenable))
//                    .setContentText(getResources().getString(R.string.open_location_settings))
//                    .setConfirmText(getResources().getString(R.string.yes))
//                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sDialog) {
//                           runpermission();
//                        }
//                    })
//                    .show();
//
//        }else {
            if(isNetworkAvailable())
            {
                if (new SessionManager(SplachScreen.this).isLoggedIn())
                {
                    getDriverPendingBooking();
                }
                else
                {
                    startActivity(new Intent(SplachScreen.this, Intro.class));
                    finish();
                }
            }else {

                startActivity(new Intent(SplachScreen.this,NOInternetActivity.class));
            }
        //}


    }

    /**
     * Change the app lang
     * @param context
     */
    public void changeLang(Context context)
    {
        if (Constants.getChangeLang(context).equals(""))
        {


            Constants.ChangeLang("ar", context);
            Locale locale = new Locale(Constants.getChangeLang(context));
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = locale;
            res.updateConfiguration(conf, dm);
        }
        else
        {
            Log.e("Errror==>", "language is: not empty:" + Constants.getChangeLang(context));
            Locale locale = new Locale(Constants.getChangeLang(context));
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = locale;
            res.updateConfiguration(conf, dm);

            /**
             * if you want to update the map lang uncomment this code
             * */
//            String languageToLoad = Constants.getChangeLang(SplachScreen.this);
//            Locale locale = new Locale(languageToLoad);
//            Locale.setDefault(locale);
//            Configuration config = new Configuration();
//            config.locale = locale;
//            getBaseContext().getResources().updateConfiguration(config,
//                    getBaseContext().getResources().getDisplayMetrics());
        }
    }


    /**
     * check if there is any pending trip at the driver
     */

    private void getDriverPendingBooking()
    {
        final String TAG = "ASI";
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("DriverId", Constants.getUserId(SplachScreen.this));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL + "DriverPendingBooking", new JSONObject(postParam),
                new Response.Listener<JSONObject>()
                {


                    @Override
                    public void onResponse(JSONObject response)
                    {

                        Log.e("DriverPendingBooking", response.toString());

                        try
                        {
                            if (response.getString("Flag").equals("45"))
                            {
                                if (new SessionManager(SplachScreen.this).isLoggedIn())
                                {
                                    Intent intent = new Intent(SplachScreen.this, MainActivity.class);
                                    startActivity(intent);
                                    /**
                                     * run the service for tracing the location of the driver
                                     * */
                                    Intent serviceIntent = new Intent(SplachScreen.this, LocationTrack.class);
                                    startService(serviceIntent);
                                    finish();
                                }
                                else
                                {
                                    startActivity(new Intent(SplachScreen.this, Intro.class));
                                    /**
                                     * run the service for tracing the location of the driver
                                     * */
                                    Intent serviceIntent = new Intent(SplachScreen.this, LocationTrack.class);
                                    startService(serviceIntent);
                                    finish();
                                }
                            }
                            else
                            {
                                DriverPendingBooking holdingTripData = new Gson().fromJson(response.toString(), DriverPendingBooking.class);
                                Log.e("DriverPendingBooking", response.toString());
                                //Log.e("Holding Trip is -->",holdingTripData.toString());
                                if (holdingTripData.getTravelStatus().equals("2"))
                                {
                                    Toasty.error(SplachScreen.this,getResources().getString(R.string.in_progress), Toast.LENGTH_LONG, true).show();
                                    AppController.saveNewTripData(holdingTripData.getPickUpPlace(), holdingTripData.getDropPlace(), holdingTripData.getPassengerName(), holdingTripData.getTripId(), holdingTripData.getDropLatitude(), holdingTripData.getDropLongitude(), holdingTripData.getPickupLatitude(), holdingTripData.getPickupLongitude(), holdingTripData.getPassengerId(), "5", holdingTripData.getPassengerPhone(), holdingTripData.getPassengerProfileImage());
                                    Intent intent=new Intent(new Intent(SplachScreen.this, ShowRoute.class));
                                    new AppController().setName("1");
                                    intent.putExtra("status","1");
                                    startActivity(intent);

                                }
                                else if (holdingTripData.getTravelStatus().equals("9"))
                                {
                                    Toasty.error(SplachScreen.this,getResources().getString(R.string.TripConfirmed), Toast.LENGTH_LONG, true).show();
                                    AppController.saveNewTripData(
                                            holdingTripData.getPickUpPlace(),
                                            holdingTripData.getDropPlace(),
                                            holdingTripData.getPassengerName(),
                                            holdingTripData.getTripId(),
                                            holdingTripData.getDropLatitude(),
                                            holdingTripData.getDropLongitude(),
                                            holdingTripData.getPickupLatitude(),
                                            holdingTripData.getPickupLongitude(),
                                            holdingTripData.getPassengerId(),
                                            "5",
                                            holdingTripData.getPassengerPhone(),
                                            holdingTripData.getPassengerProfileImage()

                                    );
                                    Constants.SaveDriverStatus(Constants.DRIVER_STATUS_GET_New_REQUEST,SplachScreen.this);
                                    startActivity(new Intent(SplachScreen.this, ShowRoute.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("status", "0"));
                                    finish();
                                }

                                else if (holdingTripData.getTravelStatus().equals("5"))
                                {
                                    Toasty.error(SplachScreen.this,getResources().getString(R.string.Waiting_for_payment), Toast.LENGTH_LONG, true).show();
                                    AppController.saveNewTripData(
                                            holdingTripData.getPickUpPlace(),
                                            holdingTripData.getDropPlace(),
                                            holdingTripData.getPassengerName(),
                                            holdingTripData.getTripId(),
                                            holdingTripData.getDropLatitude(),
                                            holdingTripData.getDropLongitude(),
                                            holdingTripData.getPickupLatitude(),
                                            holdingTripData.getPickupLongitude(),
                                            holdingTripData.getPassengerId(),
                                            holdingTripData.getPassengerPhone(),
                                            holdingTripData.getPassengerProfileImage(),
                                            holdingTripData.getDistance(),
                                            holdingTripData.getWaitingTime(),
                                            holdingTripData.getWaitingCost(),
                                            holdingTripData.getTotalFare(),
                                            holdingTripData.getTaxAmount(),
                                            holdingTripData.getPromotionDiscountAmount(),
                                            holdingTripData.getBaseFare()

                                    );
                                    Intent intent = new Intent(SplachScreen.this, TripCoastAndPay.class);
                                    intent.putExtra("PassengerId", AppController.getPassengerId());
                                    intent.putExtra("DistanceSer", AppController.getDistance());
                                    intent.putExtra("TripFare", AppController.getTripFare());
                                    intent.putExtra("WaitingTime", AppController.getWaitingTime());
                                    intent.putExtra("WaitingCost", AppController.getWaitingCost());
                                    intent.putExtra("TotalFare", AppController.getTotalFare());
                                    intent.putExtra("TaxAmount", AppController.getTaxAmount());
                                    intent.putExtra("PromotionDiscountAmount", AppController.getPromotionDiscountAmount());
                                    intent.putExtra("BaseFare", AppController.getBaseFare());
                                    startActivity(intent);
                                    finish();


                                }
                                else if (holdingTripData.getTravelStatus().equals("3"))
                                {
                                    Toasty.error(SplachScreen.this,getResources().getString(R.string.Start_to_pick), Toast.LENGTH_LONG, true).show();
                                    AppController.saveNewTripData(holdingTripData.getPickUpPlace(), holdingTripData.getDropPlace(), holdingTripData.getPassengerName(), holdingTripData.getTripId(), holdingTripData.getDropLatitude(), holdingTripData.getDropLongitude(), holdingTripData.getPickupLatitude(), holdingTripData.getPickupLongitude(), holdingTripData.getPassengerId(), "5", holdingTripData.getPassengerPhone(), holdingTripData.getPassengerProfileImage());
                                    startActivity(new Intent(SplachScreen.this, ShowRoute.class).putExtra("status", "2"));

                                }
//                                Intent intent=new Intent(SplachScreen.this,MainActivity.class);
//                                startActivity(intent);
                                /**
                                 * run the service for tracing the location of the driver
                                 * */
//                                Intent serviceIntent = new Intent(SplachScreen.this, LocationTrack.class);
//                                startService(serviceIntent);
//                                finish();
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

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                   // Toasty.error(SplachScreen.this,"TimeoutError",Toast.LENGTH_LONG,true).show();
                } else if (error instanceof AuthFailureError) {
                    //Toasty.error(SplachScreen.this,"AuthFailureError",Toast.LENGTH_LONG,true).show();
                } else if (error instanceof ServerError) {
                    //Toasty.error(SplachScreen.this,"ServerError",Toast.LENGTH_LONG,true).show();
                } else if (error instanceof NetworkError) {
                    //Toasty.error(SplachScreen.this,"NetworkError",Toast.LENGTH_LONG,true).show();
                } else if (error instanceof ParseError) {
                   // Toasty.error(SplachScreen.this,"ParseError",Toast.LENGTH_LONG,true).show();
                }
                Log.e(TAG, "Error: ****/**/" + error.getMessage());
                // hideProgressDialog();
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
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = android.provider.Settings.Secure.getInt(context.getContentResolver(), android.provider.Settings.Secure.LOCATION_MODE);

            } catch (android.provider.Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != android.provider.Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    public void runpermission() {
        if (ActivityCompat.checkSelfPermission(SplachScreen.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(SplachScreen.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(SplachScreen.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(SplachScreen.this, permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SplachScreen.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SplachScreen.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SplachScreen.this, permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SplachScreen.this, permissionsRequired[3])) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(SplachScreen.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(SplachScreen.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(SplachScreen.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs  Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        // Toast.makeText(getBaseContext(), "Go to Permissions to Grant   Location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(SplachScreen.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }


            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(SplachScreen.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SplachScreen.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SplachScreen.this, permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SplachScreen.this, permissionsRequired[3])
                    || ActivityCompat.shouldShowRequestPermissionRationale(SplachScreen.this, permissionsRequired[4])) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SplachScreen.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Calls and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(SplachScreen.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                // Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(SplachScreen.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private void proceedAfterPermission() {
        // txtPermissions.setText("We've got all permissions");
        //  Toast.makeText(getBaseContext(), "We got All Permissions", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(SplachScreen.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

}
