package com.example.geomhelper.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.geomhelper.Person;
import com.example.geomhelper.R;

import static android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences;

public class FragmentSettings extends PreferenceFragmentCompat {

    //    CheckBoxPreference checkBoxNight, checkBoxDayNightAuto;
    ListPreference listPreference;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    Activity mCurrentActivity;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        mCurrentActivity = getActivity();
        getDefaultSharedPreferences(getContext());
        setPreferencesFromResource(R.xml.preferences, rootKey);
        try {
            mSettings = mCurrentActivity.getSharedPreferences(Person.APP_PREFERENCES, Context.MODE_PRIVATE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        listPreference = (ListPreference) findPreference("pref_day_night");
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                editor = mSettings.edit();
                if (newValue.equals("Включен")) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    try {
                        mCurrentActivity.finish();
                        mCurrentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        mCurrentActivity.startActivity(mCurrentActivity.getIntent());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    editor.putString("pref_day_night","Включен");
                } else if(newValue.equals("Выключен")) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    try {
                        mCurrentActivity.finish();
                        mCurrentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        mCurrentActivity.startActivity(mCurrentActivity.getIntent());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    editor.putString("pref_day_night","Выключен");
                } else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
                    try {
                        mCurrentActivity.finish();
                        mCurrentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        mCurrentActivity.startActivity(mCurrentActivity.getIntent());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    editor.putString("pref_day_night","Авто");
                }
                editor.putBoolean("fragment_settings",true);
                editor.apply();
                return true;
            }
        });
    }

}
