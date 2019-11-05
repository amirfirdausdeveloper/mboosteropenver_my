package org.app.mbooster.Helper;

import android.util.Log;

public class LogHelper {

    public static final String TAG = "MBooster";

    public static final int maxLogSize = 1000;

    /**
     * Set Log.Error for release
     * Set Log.Debug for testing
     */
    public static int LogLevel = Log.ERROR;

    public static void debug(String msg){
        if(LogLevel > Log.DEBUG){
            return;
        }
        for(int i = 0; i <= msg.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > msg.length() ? msg.length() : end;
            Log.v(TAG, msg.substring(start, end));
        }
    }

    public static void error(String msg){
        if(LogLevel > Log.ERROR){
            return;
        }
        for(int i = 0; i <= msg.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > msg.length() ? msg.length() : end;
            Log.e(TAG, msg.substring(start, end));
        }
    }

    public static void info(String msg){
        if(LogLevel > Log.INFO){
            return;
        }
        for(int i = 0; i <= msg.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > msg.length() ? msg.length() : end;
            Log.i(TAG, msg.substring(start, end));
        }
    }

    public static void VERBOSE(String msg){
        if(LogLevel > Log.VERBOSE){
            return;
        }
        for(int i = 0; i <= msg.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > msg.length() ? msg.length() : end;
            Log.v(TAG, msg.substring(start, end));
        }
    }

}
