package com.asi.yalla_egy.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * this BroadcastReceiver will start the the service automatic if the mobile rebooted
 * */
public class StartServiceAginBroadcast extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			context.startService(new Intent(context, LocationTrack.class));
		}else {
			context.startService(new Intent(context, LocationTrack.class));
		}
	}


}
