package com.example.geomhelper.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText mEmail;
    private EditText mPassword;
    private EditText mName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                        if (!task.isSuccessful())
                            Toast.makeText(LoginActivity.this, "Регистрация провалена.",
                                    Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful())
                            Toast.makeText(LoginActivity.this, "Вход провален",
                                    Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
        switch (v.getId()) {
            case R.id.sign_in:
                if (!validateForm(false)) return;
                signIn(mEmail.getText().toString(), mPassword.getText().toString());
                intent.putExtra("reg", 0);
                break;
            case R.id.sign_up:
                if (!validateForm(true)) return;
                createAccount(mEmail.getText().toString(), mPassword.getText().toString());
                Person.name = mName.getText().toString();
                intent.putExtra("reg", 1);
                break;
        }
        startActivityForResult(intent, 1);
    }

}




