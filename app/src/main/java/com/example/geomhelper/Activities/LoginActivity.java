package com.example.geomhelper.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.geomhelper.Courses;
import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText mEmail;
    private EditText mPassword;
    private EditText mName;

    ProgressDialog progressDialog;

    boolean d = true, e = true;

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
            recreate();
        }
        setContentView(R.layout.activity_login);

        //firebase
        mAuth = FirebaseAuth.getInstance();

        //views
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mName = findViewById(R.id.person_name);

        //buttons
        findViewById(R.id.sign_up).setOnClickListener(this);
        findViewById(R.id.sign_in).setOnClickListener(this);

    }

    private void createAccount(String email, String key) {
        mAuth.createUserWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            e = false;
                            Toast.makeText(LoginActivity.this, "Регистрация провалена.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            e = false;
                            Toast.makeText(LoginActivity.this, "Вход провален",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm(boolean q) {
        boolean valid = true;

        String name = mName.getText().toString();
        if (q && TextUtils.isEmpty(name)) {
            mName.setError("Заполните поле");
            valid = false;
        }

        String email = mEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Заполните поле");
            valid = false;
        }

        String password = mPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPassword.setError("Заполните поле");
            valid = false;
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        int num = -1;
        switch (v.getId()) {
            case R.id.sign_in:
                if (!validateForm(false)) return;
                signIn(mEmail.getText().toString(), mPassword.getText().toString());
                num = 0;
                break;
            case R.id.sign_up:
                if (!validateForm(true)) return;
                createAccount(mEmail.getText().toString(), mPassword.getText().toString());
                Person.name = mName.getText().toString();
                num = 1;
                break;
        }
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Загрузка...");
        progressDialog.show();
        Async async = new Async();
        async.onPreExecute();
        async.execute(num);
    }

    private class Async extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            while (Person.uId == null) {
                if (e) {
                    try {
                        Thread.sleep(50);
                        Person.uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    publishProgress();
                    return null;
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

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.hide();
            e = true;
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

    @Override
    public void onBackPressed() {

    }
}




