package com.github.aliakseiKaraliou.onechatapp.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.github.aliakseiKaraliou.onechatapp.R;

public class PreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setFragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PregerencesFragment()).commit();

    }

    private class PregerencesFragment extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref);
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                    try {
                        final CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference(s);
                        boolean b = checkBoxPreference.isChecked();
                        setTheme(b ? R.style.LightTheme : R.style.LightTheme);
                        PreferencesActivity.this.recreate();
                        new StringBuilder();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
