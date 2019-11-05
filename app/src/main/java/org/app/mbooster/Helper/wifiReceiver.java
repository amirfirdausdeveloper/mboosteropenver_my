package org.app.mbooster.Helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ITP on 5/22/2017.
 */

public class wifiReceiver extends BroadcastReceiver {
    public static boolean connected = false;
    @Override
    public void onReceive(final Context context, final Intent intent) {

      //  String status = SplashActivity.getConnectivityStatusString(context);

       // Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }

}
