package com.example.geomhelper.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.example.geomhelper.User;
import com.example.geomhelper.UserService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirm;
    private EditText mName;
    private CircleImageView circleImageView;
    ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        //action bar
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
            recreate();
        }

        setContentView(R.layout.activity_sign_up);

        progressDialog = new ProgressDialog(SignUp.this);

        //views
        mEmail = findViewById(R.id.email_sign_up);
        mPassword = findViewById(R.id.password_sign_up);
        mConfirm = findViewById(R.id.confirm_sign_up);
        mName = findViewById(R.id.person_name);
        circleImageView = findViewById(R.id.imageProfileSignUp);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(SignUp.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 6);
            }
        });

        findViewById(R.id.sign_up).setOnClickListener(this);

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

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager)
                getApplicationContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        if (v.getId() == R.id.sign_up) {
            if (!validateForm()) return;

            progressDialog.setTitle("Загрузка...");
            progressDialog.show();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(User.URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            UserService userService = retrofit.create(UserService.class);
            userService.createUser(mEmail.getText().toString(),
                    mPassword.getText().toString(),
                    mName.getText().toString())
                    .enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    switch (Objects.requireNonNull(response.body())) {
                        case "0":
                            Toast.makeText(SignUp.this, "Произошла ошибка!",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case "2":
                            Toast.makeText(SignUp.this,
                                    "Пользователь с таким email уже зарегестрирован!",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Person.uId = response.body();
                            Person.name = mName.getText().toString();

                            SharedPreferences mSettings = getSharedPreferences(
                                    Person.APP_PREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = mSettings.edit();
                            editor.putBoolean(Person.APP_PREFERENCES_WELCOME, true);
                            editor.putString(Person.APP_PREFERENCES_UID, Person.uId);
                            editor.putString(Person.APP_PREFERENCES_NAME, Person.name);
                            editor.apply();

                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            finish();
                            CharSequence text = "Добро пожаловать, " + mName.getText() + "!";
                            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                            break;
                    }
                    progressDialog.cancel();
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Toast.makeText(getApplicationContext(), "Произошла ошибка.",
                            Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 6) {
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
            SharedPreferences.Editor editor = getSharedPreferences(
                    Person.APP_PREFERENCES, Context.MODE_PRIVATE).edit();
            editor.putBoolean("image", true);
            editor.apply();
        }
    }
}

