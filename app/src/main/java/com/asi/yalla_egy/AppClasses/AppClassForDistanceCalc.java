package com.asi.yalla_egy.AppClasses;

import com.asi.yalla_egy.AppController;

/**
 * Created by m.khalid on 5/10/2017.
 */

public class AppClassForDistanceCalc extends AppController
{

    public AppClassForDistanceCalc mInstance;

   static String newLAt,newLong,oldLat,oldLong;

    static double saveOldDis;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mInstance = this;
    }

    /**
     * this app class will hold data to calc dis
     * */

    public static String getNewLAt()
    {
        return newLAt;
    }

    public static void setNewLAt(String newLAt)
    {
        AppClassForDistanceCalc.newLAt = newLAt;
    }

    public static String getNewLong()
    {
        return newLong;
    }

    public static void setNewLong(String newLong)
    {
        AppClassForDistanceCalc.newLong = newLong;
    }

    public static String getOldLat()
    {
        return oldLat;
    }

    public static void setOldLat(String oldLat)
    {
        AppClassForDistanceCalc.oldLat = oldLat;
    }

    public static String getOldLong()
    {
        return oldLong;
    }

    public static void setOldLong(String oldLong)
    {
        AppClassForDistanceCalc.oldLong = oldLong;
    }


   // -------------------------

    public static double getSaveOldDis()
    {
        return saveOldDis;
    }

    public static void setSaveOldDis(double saveOldDis)
    {
        AppClassForDistanceCalc.saveOldDis = saveOldDis;
    }
}
