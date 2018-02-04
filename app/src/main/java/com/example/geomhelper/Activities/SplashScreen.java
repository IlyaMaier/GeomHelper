package com.example.geomhelper.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.example.geomhelper.Courses;
import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        WebView webView = findViewById(R.id.web_loading);
        webView.loadUrl("file:///android_asset/loading.html");

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Thread thread1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getDataFromFirebase();
                        }
                    });
                    thread1.start();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    SharedPreferences mSettings = getSharedPreferences(Person.APP_PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putBoolean(Person.APP_PREFERENCES_WELCOME, true);
                    editor.apply();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        thread.start();
    }

    void getDataFromFirebase() {
        try {
            Person.uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        DatabaseReference f = FirebaseDatabase.getInstance().getReference();
        f.child(Person.uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Person.name = dataSnapshot.child("name").getValue(String.class);
                for (int i = 0; i < Courses.currentCourses.size(); i++) {
                    if ("added".equals(dataSnapshot.child("courses").child(String.valueOf(i)).getValue(String.class))) {
                        if (!Person.courses.contains(Courses.currentCourses.get(i)))
                            Person.courses.add(Courses.currentCourses.get(i));
                    } else {
                        Person.courses.remove(Courses.currentCourses.get(i));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
