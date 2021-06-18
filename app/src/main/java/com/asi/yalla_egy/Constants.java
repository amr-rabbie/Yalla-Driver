package com.asi.yalla_egy;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.util.DisplayMetrics;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import LoingSession.SQLiteHandler;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ASI on 4/5/2017.
 */

 public class Constants {
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String MyPREFERENCES_BOOL="pref";

//    public static final String BASE_URL="http://5.42.254.19/YallaWebSaudi/api/yallaservice/";
    public static final String BASE_URL="http://197.161.95.169:81/YallaWebSaudi/api/yallaservice/";
    //public static final String BASE_URL="http://5.42.254.19/YallaWeb/api/yallaservice/";
    public static final String DRIVER_STATUS_FREE="F";
    public static final String DRIVER_STATUS_IN_TRIP="A";
    public static final String DRIVER_STATUS_GET_New_REQUEST="B";



    // For Yalla Flag Code
    public static final String SUCCSESS="0";
    public static final String INVALID_REQUEST="1";
    public static final String INVALID_USER="2";
    public static final String PASSWORD_CHANGED="3";
    public static final String OLD_PASS_INCORRECT="4";
    public static final String PHONE_NOT_EXISTS="5";
    public static final String PASSWORD_FAILED="6";
    public static final String ACCOUNT_DEACTIVTE="7";
    public static final String ALREADY_LOGIN="8";
    public static final String USER_BLOCKED="9";
    public static final String DRIVER_NOT_LOGIN="10";

    public static final String DRIVER_IN_TRIP="13";
    public static final String DRIVER_SHIFT="15";
    public static final String DRIVER_SHIFT_OUT="16";
    public static final String TAXI_NOT_ASSIGNED="18";
    public static final String REQUEST_REJECTED_BY_DRIVER="21";
    public static final String DRIVER_REPLY_TIMEOUT="22";
    public static final String INVALID_TRIP="25";
    public static final String DRIVER_ACCEPTED_THE_TRIP_REQUEST="26";
    public static final String TRIP_ALREADY_CONFIRMED="27";
    public static final String MESSAGE_SEND_WITH_New_PASS="48";
    public static final String AMOUNT_ADD_SUCESSFULLY="51";
    public static final String INSUFFICIENT_WALLET_AMOUNT="40";
    public static final String TOTAL_TIME_STATUS="totalTimeStatus";

    public  static  void ChangeLang(String value, Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE).edit();
        editor.putString("langlang", value);
        editor.apply();
    }
    public  static  String getChangeLang( Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return prefs.getString("langlang","");
    }




    /**
     * This is method for convert the string value into MD5
     */
    public static String convertPassMd5(String pass) {

        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            pass = new BigInteger(1, mdEnc.digest()).toString(16);
            while (pass.length() < 32) {
                pass = "0" + pass;
            }
            password = pass;
        }
        catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return password;
    }







    public  static  void SaveDriverStatus(String value, Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE).edit();
        editor.putString("status", value);
        editor.apply();
    }
    public  static  String getDriverStatus( Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return prefs.getString("status","F");
    }


    public  static  void saveDistanceKm(String value, Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE).edit();
        editor.putString("disKm", value);
        editor.apply();
    }
    public  static  String getDistanceKm( Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return prefs.getString("disKm","0");
    }

    public  static  void saveTime(String value, Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE).edit();
        editor.putString("time", value);
        editor.apply();
    }
    public  static  String getTime( Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return prefs.getString("time", "");
    }



    public static String getUserId(Context context)
    {
        return new SQLiteHandler(context).getUserDetails().get("uid");
    }




    /**
     * if you want to save the new trip to make it shared from any place
     * */
    public  static  void SavedISTANCE(String value, Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE).edit();
        editor.putString("DIS", value);
        editor.apply();
    }
    public  static  String getdISTANCE( Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return prefs.getString("DIS","0");
    }


    /**
     * this method return address from latlang
     * */
    public static String getCompleteAddressString(Context context,double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                strAdd = addresses.get(0).getAddressLine(0);
             //   Address returnedAddress = addresses.get(0).getAddressLine(0);
//                StringBuilder strReturnedAddress = new StringBuilder("");
//
//                for (int i = 0; i < address.getMaxAddressLineIndex; i++) {
//                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(",");
//                }
//                strAdd = strReturnedAddress.toString();
                // Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                // Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }



    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public  static  void saveNotificationNumber(String value, Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE).edit();
        editor.putString("noti", value);
        editor.apply();
    }
    public  static  String getNotificationNumber( Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return prefs.getString("noti","0");
    }






    public  static  void SavedDriverShift(String value, Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE).edit();
        editor.putString("shift", value);
        editor.apply();
    }
    public  static  String getDriverShift( Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return prefs.getString("shift","1");
    }


    // to get the local lang
    public static void getLocallang(Context context)
    {
        Locale locale = new Locale(Constants.getChangeLang(context));
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
    }


    public  static  void setIsFirstOpen(Boolean value, Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MyPREFERENCES_BOOL,MODE_PRIVATE).edit();
        editor.putBoolean("noti", value);
        editor.apply();
    }
    public  static  Boolean isFirestOpen( Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES_BOOL, MODE_PRIVATE);
        return prefs.getBoolean("noti",false);
    }

    public static String addingTwoTime(String s1,String s2){
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));


        Date date1 = null;
        Date date2 = null;
        try {
            date1 = timeFormat.parse(s1);
            date2 = timeFormat.parse(s2);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        long sum = date1.getTime() + date2.getTime();

        String date3 = timeFormat.format(new Date(sum));
        return date3;


    }


}
