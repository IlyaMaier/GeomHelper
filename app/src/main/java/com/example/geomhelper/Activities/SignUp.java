package com.example.geomhelper.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.Resources.CircleImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.kinvey.android.Client;
import com.kinvey.android.callback.AsyncUploaderProgressListener;
import com.kinvey.android.model.User;
import com.kinvey.android.store.FileStore;
import com.kinvey.android.store.UserStore;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.core.MediaHttpUploader;
import com.kinvey.java.model.FileMetaData;
import com.kinvey.java.store.StoreType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirm;
    private EditText mName;
    private CircleImageView circleImageView;

    ProgressDialog progressDialog;

    boolean g = false;

    Client mKinveyClient;

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
            recreate();
        }
        setContentView(R.layout.activity_sign_up);

        //views
        mEmail = findViewById(R.id.email_sign_up);
        mPassword = findViewById(R.id.password_sign_up);
        mConfirm = findViewById(R.id.confirm_sign_up);
        mName = findViewById(R.id.person_name);
        circleImageView = findViewById(R.id.imageProfileSignUp);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 6);
            }
        });

        //buttons
        findViewById(R.id.sign_up).setOnClickListener(this);

        mKinveyClient = new Client.Builder("kid_B1OS_p1hM",
                "602d7fccc790477ca6505a1daa3aa894",
                this.getApplicationContext()).setBaseUrl("https://baas.kinvey.com").build();

    }

    private boolean validateForm() {
        boolean valid = true;

        String name = mName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            mName.setError("Заполните поле");
            valid = false;
        }

        String email = mEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Заполните поле");
            valid = false;
        }

        String password = mPassword.getText().toString();

        if (password.length() < 6) {
            mPassword.setError("Пароль не может быть меньше 6 символов!");
            valid = false;
        }

        if (TextUtils.isEmpty(password)) {
            mPassword.setError("Заполните поле");
            valid = false;
        }

        if (!password.equals(mConfirm.getText().toString())) {
            mConfirm.setError("Пароли не совпадают");
            valid = false;
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        if (v.getId() == R.id.sign_up) {
            if (!validateForm()) return;
            UserStore.logout(mKinveyClient, new KinveyClientCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
            UserStore.signUp(mEmail.getText().toString(),
                    mPassword.getText().toString(), mKinveyClient,
                    new KinveyClientCallback<User>() {
                        @Override
                        public void onFailure(Throwable t) {
                            CharSequence text = "Регистрация провалена.";
                            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }

                        @Override
                        public void onSuccess(User u) {
                            CharSequence text = "Добро пожаловать, " + mName.getText() + "!";
                            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                            intent();
                        }
                    });
        }
        progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setTitle("Загрузка...");
        progressDialog.show();
        FirebaseAuth.getInstance().signInWithEmailAndPassword("qwerty@gmail.com", "qwerty");
    }

    void intent() {
        User user = mKinveyClient.getActiveUser();

        Person.name = mName.getText().toString();
        Person.uId = user.getId();

        if (!g) {
            Bitmap yourSelectedImage = BitmapFactory.decodeResource(
                    getResources(), R.drawable.back_login);
            try {
                File f = new File(getFilesDir(), "profileImage.png");
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(f);
                    yourSelectedImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                } finally {
                    if (fos != null) fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        final java.io.File file = new java.io.File(getFilesDir(), "profileImage.png");
        final boolean isCancelled = false;
        FileStore fileStore = mKinveyClient.getFileStore(StoreType.CACHE);
        try {
            fileStore.upload(file, new AsyncUploaderProgressListener<FileMetaData>() {
                @Override
                public void onSuccess(FileMetaData fileMetaData) {
                    Person.id = fileMetaData.getId();
                    Person.map.put("name", Person.name);
                    Person.map.put("experience", Person.experience);
                    Person.map.put("courses", "");
                    Person.map.put("image", Person.id);

                    Client client = new Client.Builder("kid_B1OS_p1hM",
                            "602d7fccc790477ca6505a1daa3aa894",
                            getApplicationContext()).setBaseUrl("https://baas.kinvey.com").build();
                    User user = client.getActiveUser();
                    user.putAll(Person.map);
                    user.update(new KinveyClientCallback() {
                        @Override
                        public void onSuccess(Object o) {

                        }

                        @Override
                        public void onFailure(Throwable throwable) {

                        }
                    });
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Toast.makeText(getApplicationContext(), "Не удалось загрузить изображение.",
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void progressChanged(MediaHttpUploader mediaHttpUploader) throws IOException {
                }

                @Override
                public void onCancelled() {
                }

                @Override
                public boolean isCancelled() {
                    return isCancelled;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Не удалось загрузить изображение.",
                    Toast.LENGTH_SHORT).show();
        }

        SharedPreferences mSettings = getSharedPreferences(Person.APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(Person.APP_PREFERENCES_WELCOME, true);
        editor.putString(Person.APP_PREFERENCES_UID, Person.uId);

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        editor.putString(Person.APP_PREFERENCES_NAME, Person.name);
        editor.apply();
        startActivity(i);
        finish();
        progressDialog.cancel();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 6:
                if (resultCode == RESULT_OK) {
                    Bitmap yourSelectedImage;
                    Uri selectedImage = data.getData();
                    InputStream imageStream = null;
                    try {
                        if (selectedImage != null) {
                            imageStream = getContentResolver().openInputStream(selectedImage);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    circleImageView.setImageBitmap(yourSelectedImage);
                    try {
                        File file = new File(getFilesDir(), "profileImage.png");
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(file);
                            yourSelectedImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        } finally {
                            if (fos != null) fos.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SharedPreferences.Editor editor = getSharedPreferences(Person.APP_PREFERENCES, Context.MODE_PRIVATE).edit();
                    editor.putBoolean("image", true);
                    editor.apply();
                    g = true;
                }
        }
    }
}




