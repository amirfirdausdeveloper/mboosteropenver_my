package org.app.mbooster.model_folder;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by User on 30/8/2016.
 */
public class ConnectionDetector {
    private Context _context;
    ConnectivityManager connectivityManager;
    Boolean connected = false;

    public ConnectionDetector(Context context){
        this._context = context;
    }

    /**
     * Checking for all possible internet providers
     * **/
    public boolean isConnectingToInternet(){
        try {
            connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo!= null && networkInfo.isAvailable() && networkInfo.isConnected();
            return connected;
        }
        catch (Exception e){
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
        }

        return connected;
    }
}
