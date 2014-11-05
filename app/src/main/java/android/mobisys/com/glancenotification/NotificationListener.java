package android.mobisys.com.glancenotification;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by mahavir on 11/5/14.
 */
public class NotificationListener extends NotificationListenerService {

    private static final String TAG = "NotificationListener";

    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        if(Constants.DEBUG){
            Log.d(TAG,"**********  onNotificationPosted");
            Log.d(TAG,"ID :" + statusBarNotification.getId() + "\t" + statusBarNotification.getNotification().tickerText + "\t" + statusBarNotification.getPackageName());
        }

        if(isDNDActive()){
            cancelNotification(statusBarNotification.getPackageName(),statusBarNotification.getTag(), statusBarNotification.getId());
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {

    }

    private boolean isDNDActive(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return preferences.getBoolean(Constants.PREFS_KEY_DND, false);
    }
}
