package android.mobisys.com.glancenotification.util;

import android.content.Context;
import android.content.SharedPreferences;
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
}
