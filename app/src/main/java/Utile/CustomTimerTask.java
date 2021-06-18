package Utile;

/**
 * Created by ASI on 4/6/2017.
 */


import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class CustomTimerTask extends TimerTask{

private Context context;
    private Handler mHandler = new Handler();

    public CustomTimerTask(Context con) {
        this.context = con;

    }



    @Override
    public void run() {
        new Thread(new Runnable() {

            public void run() {

                mHandler.post(new Runnable() {
                    public void run() {
                       // Toast.makeText(context, "DISPLAY YOUR MESSAGE", Toast.LENGTH_SHORT).show();
                        final ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT,150);


                    }
                });
            }
        }).start();

    }


}
