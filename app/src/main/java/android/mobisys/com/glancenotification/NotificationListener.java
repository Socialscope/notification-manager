package android.mobisys.com.glancenotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.mobisys.com.glancenotification.util.AppUtil;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mahavir on 11/5/14.
 */
public class NotificationListener extends NotificationListenerService {

    private static final String TAG = "NotificationListener";
    public static final String DND_INACTIVE = "com.mobisys.android.dnd_inactive";
    private ArrayList<StatusBarNotification> mNotifications = new ArrayList<StatusBarNotification>();
    private boolean isRegistered = false;

    /*@Override
    public void onListenerConnected(){
        Log.d(TAG, "Registering Broadcast..");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DND_INACTIVE);
        registerReceiver(mReceiver, intentFilter);
    }*/

    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        if(Constants.DEBUG){
            Log.d(TAG, "**********  onNotificationPosted");
            Log.d(TAG, "ID :" + statusBarNotification.getId() + "\t" + statusBarNotification.getNotification().tickerText + "\t" + statusBarNotification.getPackageName());
        }

        if(!isRegistered){
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(DND_INACTIVE);
            registerReceiver(mReceiver, intentFilter);
            isRegistered = true;
        }

        if(AppUtil.isDNDActive(getApplicationContext())){
            mNotifications.add(0,statusBarNotification);
            cancelNotification(statusBarNotification.getPackageName(), statusBarNotification.getTag(), statusBarNotification.getId());
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {

    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Received Broadcast..");
            if(DND_INACTIVE.equals(intent.getAction())) showAllNotifications();
        }
    };

    private void showAllNotifications(){
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        for(int i=0; i<mNotifications.size(); i++){
            StatusBarNotification notification = mNotifications.get(i);
            int id = notification.getId();
            if(map.containsKey(notification.getId())){
                String packageName = map.get(notification.getId());
                if(!packageName.equals(notification.getPackageName())) showNotification(i+1,notification);
            } else {
                showNotification(i+1, notification);
            }
            map.put(notification.getId(), notification.getPackageName());
        }

        mNotifications.clear();
    }

    private void showNotification(int i, StatusBarNotification notification){
        Notification notification1 = notification.getNotification().clone();
        notification1.icon = R.drawable.ic_launcher;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notification.getId(), notification1);
    }
}
