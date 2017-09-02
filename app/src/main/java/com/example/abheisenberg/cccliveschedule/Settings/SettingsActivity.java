package com.example.abheisenberg.cccliveschedule.Settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;

import com.example.abheisenberg.cccliveschedule.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle("Settings");

        final CheckBox cb_alarmEarly = (CheckBox) findViewById(R.id.cb_settings_alarmearly);
        cb_alarmEarly.setChecked(UserPreferences.getAlarmEarly(this));
        cb_alarmEarly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserPreferences.setAlarmEarly(SettingsActivity.this, cb_alarmEarly.isChecked());
            }
        });
    }
}
