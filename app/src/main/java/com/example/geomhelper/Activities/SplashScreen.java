package com.example.geomhelper.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.Toast;

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

    boolean d = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        WebView webView = findViewById(R.id.web_loading);
        webView.loadUrl("file:///android_asset/loading.html");

        int i = getIntent().getIntExtra("reg", -1);

        Async async = new Async();
        async.onPreExecute();
        async.execute(i);
    }

    private class Async extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            while (Person.uId == null) {
                try {
                    Thread.sleep(10);
                    Person.uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            boolean q = false;
            if (integers[integers.length - 1] == 0) {
                getDataFromFirebase();
                q = true;
            }
            if (integers[integers.length - 1] == 1)
                sendDataToFirebase();

            SharedPreferences mSettings = getSharedPreferences(Person.APP_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean(Person.APP_PREFERENCES_WELCOME, true);

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            while (d && q) {
            }
            editor.putString(Person.APP_PREFERENCES_NAME, Person.name);
            editor.apply();
            startActivity(i);
            finish();

            return null;
        }

    }

    void getDataFromFirebase() {
        DatabaseReference f = FirebaseDatabase.getInstance().getReference();
        f.child(Person.uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Person.name = dataSnapshot.child("name").getValue(String.class);
                if (Person.name != null && d) {
                    Toast.makeText(getApplicationContext(), "Добро пожаловать, " + Person.name + "!", Toast.LENGTH_LONG).show();
                    d = false;
                }
                for (int i = 0; i < Courses.currentCourses.size(); i++) {
                    if ("added".equals(dataSnapshot.child("courses").child(String.valueOf(i)).getValue(String.class))) {
                        if (!Person.courses.contains(Courses.currentCourses.get(i)))
                            Person.courses.add(Courses.currentCourses.get(i));
                    } else {
                        Person.courses.remove(Courses.currentCourses.get(i));
                    }
                }
                try {
                    Person.experience = Integer.parseInt(dataSnapshot.child("experience").getValue().toString());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void sendDataToFirebase() {
        DatabaseReference f = FirebaseDatabase.getInstance().getReference();
        f.child(Person.uId).child("name").setValue(Person.name);
        f.child(Person.uId).child("experience").setValue(Person.experience + "");
    }
}
