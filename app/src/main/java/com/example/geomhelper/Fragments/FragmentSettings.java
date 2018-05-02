package com.example.geomhelper.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.geomhelper.Activities.LoginActivity;
import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyPurgeCallback;
import com.kinvey.android.store.UserStore;
import com.kinvey.java.core.KinveyClientCallback;

import java.util.Objects;

public class FragmentSettings extends PreferenceFragmentCompat {

    Preference share, news, aboutAuthors, about, acc;
    EditTextPreference editTextPreferenceName;
    ListPreference listPreference;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    Activity mCurrentActivity;
    AlertDialog.Builder builder;
    Client mKinveyClient;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        mCurrentActivity = getActivity();
        setPreferencesFromResource(R.xml.preferences, rootKey);

        mKinveyClient = new Client.Builder("kid_B1OS_p1hM",
                "602d7fccc790477ca6505a1daa3aa894",
                Objects.requireNonNull(this.getContext())).setBaseUrl(
                "https://baas.kinvey.com").build();

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
                    if (name.length() > 20) name = name.substring(0, 20);
                    Person.name = name;
                    Person.map.put("name", Person.name);
                    mKinveyClient.getActiveUser().putAll(Person.map);
                    mKinveyClient.getActiveUser().update(new KinveyClientCallback() {
                        @Override
                        public void onSuccess(Object o) {

                        }

                        @Override
                        public void onFailure(Throwable throwable) {

                        }
                    });
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
                startActivity(Intent.createChooser(i, "Поделиться"));
                return false;
            }
        });

        news = findPreference("news");
        news.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Что нового");
                builder.setMessage("Добавлены анимации в курсах и тестах, " +
                        "а именно :\nПри пролистывании кнопочка Добавить исчезает под экран\n" +
                        "Все  элементы появляются плавно сверху,\n" +
                        "В курсах и текстах в курсах круглые кнопки\n" +
                        "Добавить, Вперед и Назад плавно увеличиваются в размере \n\n" +
                        "В настройках добавлены пункты\n" +
                        "'Что нового' ,\n" +
                        "'Об авторах' , \n" +
                        "'О приложении'\n");
                builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return false;
            }
        });

        aboutAuthors = findPreference("about_authors");
        aboutAuthors.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Об авторах");
                builder.setMessage("Здесь что-то будет.");
                builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return false;
            }
        });

        about = findPreference("about");
        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("О приложении");
                builder.setMessage("Одним словом - лучшее.");
                builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return false;
            }
        });

        acc = findPreference("acc");
        acc.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Выход из аккаунта");
                builder.setMessage("Вы действительно хотите выйти из аккаунта?");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        UserStore.logout(mKinveyClient, new KinveyPurgeCallback() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }

                            @Override
                            public void onFailure(Throwable throwable) {

                            }
                        });

                        LoginManager.getInstance().logOut();
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                                GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestEmail()
                                .build();

                        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                                .build();

                        mGoogleApiClient.connect();

                        if(Person.pref.getBoolean("google",false))
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(@NonNull Status status) {
                                    }
                                });

                        Person.name = "";
                        Person.uId = "";
                        Person.courses.clear();
                        Person.experience = 0;
                        Person.leaderBoardPlace = 0;
                        Person.c = "";
                        Person.id = "";
                        MainActivity.saveAll(true, false);
                        Intent i = new Intent(getContext(), LoginActivity.class);
                        startActivity(i);
                    }
                });
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setCancelable(true);
                builder.show();
                return false;
            }
        });
    }

}
