package com.example.geomhelper.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geomhelper.Content.Courses;
import com.example.geomhelper.Fragments.FragmentProfile;
import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.kinvey.android.Client;
import com.kinvey.android.callback.AsyncDownloaderProgressListener;
import com.kinvey.android.model.User;
import com.kinvey.android.store.FileStore;
import com.kinvey.android.store.UserStore;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.core.MediaHttpDownloader;
import com.kinvey.java.model.FileMetaData;
import com.kinvey.java.store.StoreType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmail;
    private EditText mPassword;

    ProgressDialog progressDialog;

    LinearLayout linearLayout;

    Client mKinveyClient;

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
//        mAuth = FirebaseAuth.getInstance();

        //views
        linearLayout = findViewById(R.id.ll_login);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        TextView textView = findViewById(R.id.not_register);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignUp.class);
                startActivity(i);
            }
        });

        //buttons
        findViewById(R.id.sign_in).setOnClickListener(this);

        mKinveyClient = new Client.Builder("kid_B1OS_p1hM",
                "602d7fccc790477ca6505a1daa3aa894",
                this.getApplicationContext()).setBaseUrl("https://baas.kinvey.com").build();

    }

    private boolean validateForm() {
        boolean valid = true;

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

        if (password.length() < 6) {
            mPassword.setError("Пароль не может быть меньше 6 символов!");
            valid = false;
        }

        return valid;
    }

    void intent() {
        User user = mKinveyClient.getActiveUser();

        Person.uId = user.getId();
        if (user.get("name") == null)
            Person.name = "";
        else Person.name = user.get("name").toString();
        if (user.get("experience") == null)
            Person.experience = 0;
        else Person.experience = Integer.parseInt(user.get("experience").toString());
        if (user.get("courses") == null)
            Person.c = "";
        else Person.c = user.get("courses").toString();
        for (int i = 0; i < Person.c.length(); i++)
            Person.courses.add(0, Courses.currentCourses.get(
                    Integer.parseInt(Person.c.charAt(i) + "")));

        File file = new File(getApplicationContext().getFilesDir(), "profileImage.png");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        FileMetaData fileMetaDataForDownload = new FileMetaData();
        String img;
        if (user.get("image") == null)
            img = "Произошла ошибка";
        else img = user.get("image").toString();
        fileMetaDataForDownload.setId(img);
        FileStore fileStore = mKinveyClient.getFileStore(StoreType.CACHE);
        try {
            final FileOutputStream finalFos = fos;
            fileStore.download(fileMetaDataForDownload, fos, new AsyncDownloaderProgressListener<FileMetaData>() {
                @Override
                public void onSuccess(FileMetaData fileMetaData) {
                    try {
                        if (finalFos != null) {
                            finalFos.close();
                        }
                        FragmentProfile.d = false;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Toast.makeText(getApplicationContext(), "Не удалось загрузить изображение.",
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void progressChanged(MediaHttpDownloader mediaHttpDownloader) {
                }

                @Override
                public void onCancelled() {
                    Toast.makeText(getApplicationContext(), "Не удалось загрузить изображение.",
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public boolean isCancelled() {
                    return false;
                }
            });
            FragmentProfile.d = true;
        } catch (IOException e1) {
            Toast.makeText(getApplicationContext(), "Не удалось загрузить изображение.",
                    Toast.LENGTH_SHORT).show();
            e1.printStackTrace();
        }

        SharedPreferences mSettings = getSharedPreferences(Person.APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(Person.APP_PREFERENCES_WELCOME, true);
        editor.putBoolean("image", true);
        editor.putString(Person.APP_PREFERENCES_UID, Person.uId);
        editor.putString(Person.APP_PREFERENCES_NAME, Person.name);
        editor.putString("id", Person.id);
        editor.putString("c", Person.c);
        editor.apply();

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();

        CharSequence text = "Добро пожаловать снова, " + Person.name + "!";
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        progressDialog.cancel();
    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        if (v.getId() == R.id.sign_in) {
            if (!validateForm()) return;
            try {
                UserStore.logout(mKinveyClient, new KinveyClientCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                });
                UserStore.login(mEmail.getText().toString(), mPassword.getText().toString(),
                        mKinveyClient, new KinveyClientCallback<User>() {
                            @Override
                            public void onFailure(Throwable t) {
                                CharSequence text = "Неправильный логин или пароль.";
                                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }

                            @Override
                            public void onSuccess(User u) {
                                intent();
                            }
                        });
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Загрузка...");
        progressDialog.show();
    }

    @Override
    public void onBackPressed() {

    }

}




