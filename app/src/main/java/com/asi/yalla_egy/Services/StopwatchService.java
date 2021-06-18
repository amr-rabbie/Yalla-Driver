package com.asi.yalla_egy.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import com.asi.yalla_egy.AppController;
import com.asi.yalla_egy.Constants;

public class StopwatchService extends Service {
	private static final String TAG = "StopwatchService";
	public static String str_receiver = "com.asi.yalla_egy.Services.receiver";



	public class LocalBinder extends Binder {
		public StopwatchService getService() {
			return StopwatchService.this;
		}
	}
	
	private Stopwatch m_stopwatch;
	private LocalBinder m_binder = new LocalBinder();
	private NotificationManager m_notificationMgr;
	private Notification m_notification;
    String time;
	// Timer to update the ongoing notification
    private final long mFrequency = 100;    // milliseconds
    private final int TICK_WHAT = 2; 
	private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
        	sendMessageDelayed(Message.obtain(this, TICK_WHAT), mFrequency);
        }
    };
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "bound");
		
		return m_binder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "created");
		m_stopwatch = new Stopwatch();
			}
	
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        if (intent == null) {
//            return Service.START_NOT_STICKY;
//        }
//        if(getFormattedElapsedTime()=="0") {

            Log.i("LocalService", "Received start id " + startId + ": " + intent);
            // We want this service to continue running until it is explicitly
            // stopped, so return sticky.

//            if (new AppController().getTimeStatus().equals("0")) {
//               String fTime= getFormattedElapsedTime();
//                Constants.saveTime(fTime, this);
//            }
//
//            } else if (new AppController().getTimeStatus().equals("1")) {
//               String lastValue=Constants.getTime(this);
//                if (lastValue==null){
//
//                    String t=Constants.addingTwoTime(lastValue,getFormattedElapsedTime());
//                    Constants.saveTime(t,this);
//
//                }
//        }
        return START_STICKY;


    }



	public void start() {
		Log.d(TAG, "start");
		m_stopwatch.start();
		
		//showNotification();
	}
	
	public void pause() {
		Log.d(TAG, "pause");
		m_stopwatch.pause();
		

	}
	
	public void lap() {
		Log.d(TAG, "lap");
		m_stopwatch.lap();
	}
	
	public void reset() {
		Log.d(TAG, "reset");
		m_stopwatch.reset();
	}
	
	public long getElapsedTime() {

		return m_stopwatch.getElapsedTime();
	}


	///////////////////////////
	public String getFormattedElapsedTime() {
		/**
		 * TODO :  if  shared  con  --> 00:00 app closed and open
		 *
		 *         key ---> time   value ---> 40:30 m:s
		 *         key ---> time   value ---> 00:00 m:s
		 *
		 * TODO : if  shared  con  --> 40:30  app closed and open
		 * */
		return formatElapsedTime(getElapsedTime());
	}
	
	public boolean isStopwatchRunning() {
		return m_stopwatch.isRunning();
	}
	
	/***
	 * Given the time elapsed in tenths of seconds, returns the string
	 * representation of that time. 
	 * 
	 * @param now, the current time in tenths of seconds
	 * @return 	String with the current time in the format MM:SS.T or 
	 * 			HH:MM:SS.T, depending on elapsed time.
	 */
	private String formatElapsedTime(long now) {
		long hours=0, minutes=0, seconds=0, tenths=0;
		StringBuilder sb = new StringBuilder();
		
		if (now < 1000) {
			tenths = now / 100;
		} else if (now < 60000) {
			seconds = now / 1000;
			now -= seconds * 1000;
			tenths = (now / 100);
		} else if (now < 3600000) {
			hours = now / 3600000;
			now -= hours * 3600000;
			minutes = now / 60000;
			now -= minutes * 60000;
			seconds = now / 1000;
			now -= seconds * 1000;
			tenths = (now / 100);
		}
		
		if (hours > 0) {
			sb.append(hours).append(":")
				.append(formatDigits(minutes)).append(":")
				.append(formatDigits(seconds)).append(".")
				.append(tenths);
		} else {
			sb.append(formatDigits(minutes)).append(":")
			.append(formatDigits(seconds)).append(".")
			.append(tenths);
		}
		
		return sb.toString();
	}
	
	private String formatDigits(long num) {
		return (num < 10) ? "0" + num : new Long(num).toString();
	}



	public String getFormattedElapsedTimeM() {
		return getMin(getElapsedTime());
	}
	private  String getMin(long now)
	{
		long hours=0, minutes=0, seconds=0, tenths=0;
		StringBuilder sb = new StringBuilder();

		if (now < 1000) {
			tenths = now / 100;
		} else if (now < 60000) {
			seconds = now / 1000;
			now -= seconds * 1000;
			tenths = (now / 100);
		} else if (now < 3600000) {
			hours = now / 3600000;
			now -= hours * 3600000;
			minutes = now / 60000;
			now -= minutes * 60000;
			seconds = now / 1000;
			now -= seconds * 1000;
			tenths = (now / 100);
		}

		if (hours > 0) {
			sb.append(hours).append(":")
					.append(formatDigits(minutes)).append(":")
					.append(formatDigits(seconds)).append(".")
					.append(tenths);
		} else {
			sb.append(formatDigits(minutes));
		}

		return sb.toString();
	}
}
