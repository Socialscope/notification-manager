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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by mahavir on 11/5/14.
 */
public class NotificationListener extends NotificationListenerService {

    private static final String TAG = "NotificationListener";
    public static final String DND_INACTIVE = "com.mobisys.android.dnd_inactive";
    private ArrayList<StatusBarNotification> mNotifications = new ArrayList<StatusBarNotification>();
    // Hashmap set of id's of all active notifications
    private HashMap<String, Integer> mHashMapIds = new HashMap<String, Integer>();
    private boolean isRegistered = false;

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

        if(AppUtil.isDNDActive(getApplicationContext()) && !statusBarNotification.getPackageName().equals(getPackageName()) && statusBarNotification.isClearable()){
            mNotifications.add(0,statusBarNotification);
            StatusBarNotification active_notification = checkIfActiveNotification(statusBarNotification);
            if(active_notification == null) cancelNotification(statusBarNotification.getPackageName(), statusBarNotification.getTag(), statusBarNotification.getId());
            else {
                cancelNotification(statusBarNotification.getPackageName(), statusBarNotification.getTag(), statusBarNotification.getId());
                active_notification.getNotification().icon = R.drawable.ic_launcher;
                showNotification(active_notification);
            }
        } else {
            String hashMapKey = getHashmapKeyForNotification(statusBarNotification);
            if(mHashMapIds.containsKey(hashMapKey)) cancelNotification(getPackageName(), null, mHashMapIds.get(hashMapKey));
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {
        if(statusBarNotification.getPackageName().equals(getPackageName())){
            Set<Map.Entry<String, Integer>> entrySet = mHashMapIds.entrySet();
            for(Map.Entry<String, Integer> entry:entrySet) {
                Integer id = entry.getValue();
                if(id == statusBarNotification.getId()) {
                    Log.d(TAG, "Removing hashmap Id ==>");
                    mHashMapIds.remove(entry.getKey());
                    break;
                }
            }
        } 
    }

    private StatusBarNotification checkIfActiveNotification(StatusBarNotification statusBarNotification){
        StatusBarNotification active_notification = null;
        StatusBarNotification[] activeNotifications = getActiveNotifications();
        for(StatusBarNotification notification:activeNotifications){
            Log.d(TAG, "ID :" + notification.getId() + "\t" + notification.getNotification().tickerText + "\t" + notification.getPackageName());
            if(notification.getId() == statusBarNotification.getId() && notification.getPackageName().equals(statusBarNotification.getPackageName())) {
                active_notification = notification;
                break;
            }
        }

        return active_notification;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Received Broadcast..");
            if(DND_INACTIVE.equals(intent.getAction())) showAllNotifications();
        }
    };

    private void showAllNotifications(){
        /*HashMap<Integer, String> map = new HashMap<Integer, String>();
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
        }*/
        for(StatusBarNotification notification:mNotifications) showNotification(notification);


        mNotifications.clear();
    }

    private void showNotification(StatusBarNotification notification){
        String notificationKey = getHashmapKeyForNotification(notification);
        int notificationId;
        if(mHashMapIds.containsKey(notificationKey)){
            notificationId = mHashMapIds.get(notificationKey);
            Log.d(TAG, "Found in Hashmap ==> "+notificationId);
        } else {
            notificationId = getNextId();
            Log.d(TAG, "Not found in Hashmap ==> "+notificationId);
        }

        Log.d(TAG, "Showing notification ==> "+notificationId+", "+notificationKey);
        Notification notification1 = notification.getNotification().clone();
        notification1.icon = R.drawable.ic_launcher;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notification1);
        mHashMapIds.put(notificationKey, notificationId);
    }

    private String getHashmapKeyForNotification(StatusBarNotification notification){
        return notification.getId()+":"+notification.getPackageName();
    }

    private int getNextId(){
        return AppUtil.getNextNotificationId(getApplicationContext());
    }
}
