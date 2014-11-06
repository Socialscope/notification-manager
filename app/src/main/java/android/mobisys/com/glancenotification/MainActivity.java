package android.mobisys.com.glancenotification;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.mobisys.com.glancenotification.util.AppUtil;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ToggleButton;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean dnd = AppUtil.isDNDActive(MainActivity.this);
        ((ToggleButton) findViewById(R.id.toggle_dnd)).setChecked(dnd);
        ((ToggleButton) findViewById(R.id.toggle_dnd)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AppUtil.saveDNDSetting(MainActivity.this, b);
                if (!b) sendDNDInactiveBroadcast();
            }
        });
    }

    private void sendDNDInactiveBroadcast(){
        Log.d("MainActivity", "Sending Broadcast..");
        Intent intent = new Intent();
        intent.setAction(NotificationListener.DND_INACTIVE);
        sendBroadcast(intent);
    }
}
