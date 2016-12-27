package com.github.aliakseiKaraliou.onechatapp.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.github.aliakseiKaraliou.onechatapp.App;
import com.github.aliakseiKaraliou.onechatapp.Constants;
import com.github.aliakseiKaraliou.onechatapp.R;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        final App application = ((App) getApplicationContext());
        sharedPreferences = application.getApplicationSharedPreferences();
        final boolean theme = sharedPreferences.getBoolean(Constants.Other.DARK_THEME, false);
        setTheme(theme ? R.style.DarkTheme : R.style.LightTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setTitle(R.string.preferences);
        }


        final int darkTheme = R.style.DarkTheme;

        final int lightTheme = R.style.LightTheme;


        final Switch nightModeSwitch = (Switch) findViewById(R.id.night_mode_switch);
        nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle(R.string.change_theme_title)
                        .setMessage(R.string.change_theme_message)
                        .setPositiveButton(getString(R.string.answer_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int which) {
                                final boolean checked = compoundButton.isChecked();
                                final SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(Constants.Other.DARK_THEME, checked);
                                editor.apply();
                                ((App) getApplicationContext()).restartApp();
                            }
                        })
                        .setNegativeButton(getString(R.string.answer_no), null).show();

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
