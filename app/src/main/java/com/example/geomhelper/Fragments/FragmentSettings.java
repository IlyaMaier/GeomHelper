package com.example.geomhelper.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.geomhelper.Activities.Others;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences;

public class FragmentSettings extends PreferenceFragmentCompat {

    Preference share,news,aboutAuthors,about;
    EditTextPreference editTextPreferenceName;
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

        editTextPreferenceName = (EditTextPreference) findPreference("name");
        editTextPreferenceName.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                editTextPreferenceName.setText(Person.name);
                return false;
            }
        });
        editTextPreferenceName.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String name = newValue.toString();
                if (name.contains("\n"))
                    name = name.replaceAll("\n", "");
                if (newValue.toString().isEmpty()) {
                    return false;
                } else {
                    Person.name = name;
                    DatabaseReference f = FirebaseDatabase.getInstance().getReference();
                    try {
                        f.child(Person.uId).child("name").setValue(name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }
        });

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
                    editor.putString("pref_day_night", "Включен");
                } else if (newValue.equals("Выключен")) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    try {
                        mCurrentActivity.finish();
                        mCurrentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        mCurrentActivity.startActivity(mCurrentActivity.getIntent());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    editor.putString("pref_day_night", "Выключен");
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
                    try {
                        mCurrentActivity.finish();
                        mCurrentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        mCurrentActivity.startActivity(mCurrentActivity.getIntent());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    editor.putString("pref_day_night", "Авто");
                }
                editor.putBoolean("fragment_settings", true);
                editor.apply();
                return true;
            }
        });

        share = findPreference("share");
        share.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT,
                        "Скачай приложение GeomHelper! Оно поможет тебе в изучении геометрии по школьной программе!" +
                                "https://yadi.sk/d/ub5kRUYy3SSHWC");
                i.setType("text/plain");
                startActivity(Intent.createChooser(i,"Поделиться"));
                return false;
            }
        });

        news = findPreference("news");
        news.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(getContext(), Others.class);
                i.putExtra("num_others",0);
                startActivity(i);
                return false;
            }
        });
        aboutAuthors = findPreference("about_authors");
        aboutAuthors.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(getContext(), Others.class);
                i.putExtra("num_others",1);
                startActivity(i);
                return false;
            }
        });
        about = findPreference("about");
        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(getContext(), Others.class);
                i.putExtra("num_others",2);
                startActivity(i);
                return false;
            }
        });
    }

}
