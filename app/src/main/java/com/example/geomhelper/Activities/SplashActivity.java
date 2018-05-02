package com.example.geomhelper.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;

import static com.example.geomhelper.Person.pref;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getSharedPreferences(Person.APP_PREFERENCES, Context.MODE_PRIVATE);
        if (!pref.getBoolean(Person.APP_PREFERENCES_WELCOME, false)) {
            Intent i = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(i);
            finish();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
