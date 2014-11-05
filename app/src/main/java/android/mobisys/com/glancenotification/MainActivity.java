package android.mobisys.com.glancenotification;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ToggleButton;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((ToggleButton)findViewById(R.id.toggle_dnd)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor edit=preferences.edit();
                edit.putBoolean(Constants.PREFS_KEY_DND, b);
                edit.commit();
            }
        });
    }
}
