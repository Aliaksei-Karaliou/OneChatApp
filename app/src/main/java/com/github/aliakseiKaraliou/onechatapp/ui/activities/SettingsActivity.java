package com.github.aliakseiKaraliou.onechatapp.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.github.aliakseiKaraliou.onechatapp.R;
import com.github.aliakseiKaraliou.onechatapp.logic.vk.Constants;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.preferences);

        new StringBuilder();
    }


    @Override
    protected void onResume() {
        super.onResume();
        final Switch nightModeSwitch = (Switch) findViewById(R.id.night_mode_switch);
        nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                final boolean checked = compoundButton.isChecked();
                setTheme(checked ? R.style.DarkTheme : R.style.LightTheme);
                recreate();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public void sendEmailToDeveloper(final View view) {
        final Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                Constants.Other.MAILTO,Constants.Other.DEVELOPER_EMAIL, null));
        startActivity(emailIntent);
    }
}
