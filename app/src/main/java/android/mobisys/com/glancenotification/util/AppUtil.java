package android.mobisys.com.glancenotification.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.mobisys.com.glancenotification.Constants;
import android.preference.PreferenceManager;

/**
 * Created by Mahavir on 11/6/14.
 */
public class AppUtil {
    public static void saveDNDSetting(Context context, boolean dnd_active){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=preferences.edit();
        edit.putBoolean(Constants.PREFS_KEY_DND, dnd_active);
        edit.commit();
    }

    public static boolean isDNDActive(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(Constants.PREFS_KEY_DND, false);
    }

    public static void saveRingerMode(Context context, int ringer_mode){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=preferences.edit();
        edit.putInt(Constants.PREFS_KEY_RINGER_MODE, ringer_mode);
        edit.commit();
    }

    public static int getRingerMode(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(Constants.PREFS_KEY_RINGER_MODE, AudioManager.RINGER_MODE_NORMAL);
    }

    public static int getNextNotificationId(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int notification_id = preferences.getInt(Constants.PREFS_KEY_NOTIFICATION_ID, 0);
        notification_id += 1;
        SharedPreferences.Editor edit=preferences.edit();
        edit.putInt(Constants.PREFS_KEY_NOTIFICATION_ID, notification_id);
        edit.commit();
        return notification_id;
    }
}
