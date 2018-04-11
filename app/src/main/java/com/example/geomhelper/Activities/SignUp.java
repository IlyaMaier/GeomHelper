package com.example.geomhelper.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.Resources.CircleImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Objects;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirm;
    private EditText mName;
    private CircleImageView circleImageView;

    ProgressDialog progressDialog;

    boolean e = true;

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
            recreate();
        }
        setContentView(R.layout.activity_sign_up);

        //firebase
        mAuth = FirebaseAuth.getInstance();

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

    }

    private void createAccount(String email, String key) {
        mAuth.createUserWithEmailAndPassword(email, key)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            e = false;
                            Toast.makeText(SignUp.this, "Регистрация провалена.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
        int num = -1;
        switch (v.getId()) {
            case R.id.sign_up:
                if (!validateForm()) return;
                createAccount(mEmail.getText().toString(), mPassword.getText().toString());
                Person.name = mName.getText().toString();
                num = 1;
                break;
        }
        progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setTitle("Загрузка...");
        progressDialog.show();
        Async async = new Async();
        async.onPreExecute();
        async.execute(num);
    }

    @SuppressLint("StaticFieldLeak")
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
                        Person.uId = Objects.requireNonNull(FirebaseAuth.
                                getInstance().getCurrentUser()).getUid();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    publishProgress();
                    return null;
                }
            }
            if (integers[integers.length - 1] == 1)
                sendDataToFirebase();

            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            Uri file = Uri.fromFile(new File(getFilesDir().getPath() +
                    "/profileImage.png"));
            try {
                StorageReference profileRef = mStorageRef.child(Person.uId);
                profileRef.putFile(file)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(getApplicationContext(),
                                        "Не удалось загрузить изображение",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (NullPointerException e) {
                e.printStackTrace();
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
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.hide();
            e = true;
        }

        void sendDataToFirebase() {
            DatabaseReference f = FirebaseDatabase.getInstance().getReference();
            f.child(Person.uId).child("name").setValue(Person.name);
            f.child(Person.uId).child("experience").setValue(Person.experience + "");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 6:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    InputStream imageStream = null;
                    try {
                        if (selectedImage != null) {
                            imageStream = getContentResolver().openInputStream(selectedImage);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
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
                }
        }
    }
}




