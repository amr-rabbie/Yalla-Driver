package com.asi.yalla_egy;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import Utile.LruBitmapCache;
import Utile.TypefaceUtil;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


/**
 * Created by ASI on 26/03/2016.
 */
public class AppController extends Application
{

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static AppController mInstance;

    static double LastLat, LastLong;
    private String timeStatus;


    public String getTimeStatus() {
        return timeStatus;
    }

    public void setTimeStatus(String timeStatus) {
        this.timeStatus = timeStatus;
    }

    public static float mapZoom;

    public static float getMapZoom()
    {
        return mapZoom;
    }

    public static void setMapZoom(float mapZoom)
    {
        AppController.mapZoom = mapZoom;
    }

    static String PickUpPlace,
            DropPlace,
            PassengerName,
            TripId,
            DropLatitude,
            DropLongitude,
            PickupLatitude,
            PickupLongitude,
            PassengerId,
            passengerRate,
            PassengerPhone,
            passengerProfileImage,
            Distance,
            TripFare,
            WaitingTime,
            WaitingCost,
            TotalFare,
            TaxAmount,
            PromotionDiscountAmount,
            BaseFare;

    @Override
    public void onCreate()
    {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mInstance = this;
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Montserrat-Regular.ttf");
    }

    public static synchronized AppController getInstance()
    {
        return mInstance;
    }

    public RequestQueue getRequestQueue()
    {
        if (mRequestQueue == null)
        {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag)
    {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader()
    {
        getRequestQueue();
        if (mImageLoader == null)
        {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public void cancelPendingRequests(Object tag)
    {
        if (mRequestQueue != null)
        {
            mRequestQueue.cancelAll(tag);
        }
    }


    String Name;

    public String getName()
    {
        return Name;
    }

    public void setName(String name)
    {
        this.Name = name;
    }


    public static void saveNewTripData(String PickUpPlace, String DropPlace, String PassengerName, String TripId, String DropLatitude, String DropLongitude, String PickupLatitude, String PickupLongitude, String PassengerId, String passengerRate, String passengerPhone, String passengerProfileImage)
    {
        AppController.PickUpPlace = PickUpPlace;
        AppController.DropPlace = DropPlace;
        AppController.PassengerName = PassengerName;
        AppController.TripId = TripId;
        AppController.DropLatitude = DropLatitude;
        AppController.DropLongitude = DropLongitude;
        AppController.PickupLatitude = PickupLatitude;
        AppController.PickupLongitude = PickupLongitude;
        AppController.PassengerId = PassengerId;
        AppController.passengerRate = passengerRate;
        AppController.PassengerPhone = passengerPhone;
        AppController.passengerProfileImage = passengerProfileImage;
    }

    public static void saveNewTripData(
            String PickUpPlace,
            String DropPlace,
            String PassengerName,
            String TripId,
            String DropLatitude,
            String DropLongitude,
            String PickupLatitude,
            String PickupLongitude,
            String PassengerId,
            String passengerPhone,
            String passengerProfileImage,
            String Distance,
            String WaitingTime,
            String WaitingCost,
            String TotalFare,
            String TaxAmount,
            String PromotionDiscountAmount,
            String BaseFare
    )
    {
        AppController.PickUpPlace = PickUpPlace;
        AppController.DropPlace = DropPlace;
        AppController.PassengerName = PassengerName;
        AppController.TripId = TripId;
        AppController.DropLatitude = DropLatitude;
        AppController.DropLongitude = DropLongitude;
        AppController.PickupLatitude = PickupLatitude;
        AppController.PickupLongitude = PickupLongitude;
        AppController.PassengerId = PassengerId;
        AppController.PassengerPhone = passengerPhone;
        AppController.passengerProfileImage = passengerProfileImage;
        AppController.Distance = Distance;
        AppController.WaitingTime = WaitingTime;
        AppController.WaitingCost = WaitingCost;
        AppController.TotalFare = TotalFare;
        AppController.TaxAmount = TaxAmount;
        AppController.PromotionDiscountAmount = PromotionDiscountAmount;
        AppController.BaseFare = BaseFare;

    }

    public static String getDropPlace()
    {
        return DropPlace;
    }

    public static String getPickUpPlace()
    {
        return PickUpPlace;
    }

    public static String getPassengerName()
    {
        return PassengerName;
    }

    public static String getTripId()
    {
        return TripId;
    }

    public static String getDropLatitude()
    {
        return DropLatitude;
    }

    public static String getDropLongitude()
    {
        return DropLongitude;
    }

    public static String getPickupLatitude()
    {
        return PickupLatitude;
    }

    public static String getPickupLongitude()
    {
        return PickupLongitude;
    }

    public static String getPassengerId()
    {
        return PassengerId;
    }

    public static String getPassengerRate()
    {
        return passengerRate;
    }

    public static String getPassengerPhone()
    {
        return PassengerPhone;
    }

    public static String getPassengerProfileImage()
    {
        return passengerProfileImage;
    }

    public static String getDistance()
    {
        return Distance;
    }

    public static String getWaitingTime()
    {
        return WaitingTime;
    }

    public static String getTripFare()
    {
        return TripFare;
    }

    public static String getWaitingCost()
    {
        return WaitingCost;
    }

    public static String getTotalFare()
    {
        return TotalFare;
    }

    public static String getPromotionDiscountAmount()
    {
        return PromotionDiscountAmount;
    }

    public static String getTaxAmount()
    {
        return TaxAmount;
    }

    public static String getBaseFare()
    {
        return BaseFare;
    }

    //-----------------------------------------------


    public static double getLastLat()
    {
        return LastLat;
    }

    public static void setLastLat(double lastLat)
    {
        LastLat = lastLat;
    }

    public static double getLastLong()
    {
        return LastLong;
    }

    public static void setLastLong(double lastLong)
    {
        LastLong = lastLong;
    }


}