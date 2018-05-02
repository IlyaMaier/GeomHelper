package com.example.geomhelper.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kinvey.android.Client;
import com.kinvey.android.callback.AsyncDownloaderProgressListener;
import com.kinvey.android.callback.AsyncUploaderProgressListener;
import com.kinvey.android.model.User;
import com.kinvey.android.store.FileStore;
import com.kinvey.android.store.UserStore;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.core.MediaHttpDownloader;
import com.kinvey.java.core.MediaHttpUploader;
import com.kinvey.java.model.FileMetaData;
import com.kinvey.java.store.StoreType;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private final int RC_SIGN_IN = 9007;
    private EditText mEmail;
    private EditText mPassword;
    private GoogleApiClient mGoogleApiClient;

    CallbackManager callbackManager;

    ProgressDialog progressDialog;

    LinearLayout linearLayout;

    Client mKinveyClient;

    protected void onCreate(Bundle savedInstanceState) {
        //action bar
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
            recreate();
        }

        setContentView(R.layout.activity_login);

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
        findViewById(R.id.vk).setOnClickListener(this);
        findViewById(R.id.facebook).setOnClickListener(this);
        findViewById(R.id.google).setOnClickListener(this);
        findViewById(R.id.twitter).setOnClickListener(this);

        mKinveyClient = new Client.Builder("kid_B1OS_p1hM",
                "602d7fccc790477ca6505a1daa3aa894",
                this.getApplicationContext()).setBaseUrl("https://baas.kinvey.com").build();

        //facebook
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                try {
                    UserStore.loginFacebook(AccessToken.getCurrentAccessToken().getToken(),
                            mKinveyClient, new KinveyClientCallback<User>() {
                                @Override
                                public void onSuccess(User user) {
                                    Person.map.put("_socialIdentity", AccessToken.getCurrentAccessToken().getToken());
                                    getMeInfo();
                                }

                                @Override
                                public void onFailure(Throwable throwable) {
                                    Toast.makeText(getApplicationContext(),
                                            "Произошла ошибка",
                                            Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();
                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(),
                        "Произошла ошибка при попытке входа через Facebook",
                        Toast.LENGTH_SHORT).show();
            }
        });

        //google+
        String serverClientId = getString(R.string.server_client_id);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
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

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        switch (v.getId()) {
            case R.id.sign_in:
                signIn();
                break;
            case R.id.vk:
                signInWithVK();
                break;
            case R.id.facebook:
                signInWithFacebook();
                break;
            case R.id.google:
                signInWithGoogle();
                break;
            case R.id.twitter:
                signInWithTwitter();
                break;
        }
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Загрузка...");
        progressDialog.show();
    }

    void signIn() {
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
            LoginManager.getInstance().logOut();
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
                            intent(false);
                        }
                    });
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    void intent(boolean facebook) {
        User user = mKinveyClient.getActiveUser();

        Person.uId = user.getId();
        if (!facebook) {
            if (user.get("name") == null)
                Person.name = "";
            else Person.name = user.get("name").toString();
        }
        if (user.get("experience") == null)
            Person.experience = 0;
        else Person.experience = Integer.parseInt(user.get("experience").toString());
        if (user.get("courses") == null)
            Person.c = "";
        else Person.c = user.get("courses").toString();
        for (int i = 0; i < Person.c.length(); i++)
            Person.courses.add(0, Courses.currentCourses.get(
                    Integer.parseInt(Person.c.charAt(i) + "")));

        if (!facebook)
            loadImage();
        saveAndFinish();
    }

    void saveAndFinish() {
        SharedPreferences mSettings = getSharedPreferences(Person.APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(Person.APP_PREFERENCES_WELCOME, true);
        editor.putBoolean("image", true);
        editor.putString(Person.APP_PREFERENCES_UID, Person.uId);
        editor.putString(Person.APP_PREFERENCES_NAME, Person.name);
        editor.putString("id", Person.id);
        editor.putString("c", Person.c);
        editor.putString("_socialIdentity", String.valueOf(Person.map.get("_socialIdentity")));
        editor.apply();

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();

        CharSequence text;
        if (Person.name != null)
            text = "Добро пожаловать снова, " + Person.name + "!";
        else text = "Добро пожаловать снова!";
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        progressDialog.cancel();
    }

    void signInWithFacebook() {
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        loginManager.logInWithReadPermissions(
                this,
                Arrays.asList("public_profile", "email"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    public void getMeInfo() {
        AccessToken token = AccessToken.getCurrentAccessToken();

        GraphRequest request = GraphRequest.newGraphPathRequest(
                token,
                "me",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        JSONObject object;
                        try {
                            if (response.getError() == null) {
                                object = new JSONObject(response.getRawResponse());
                                String name = object.getString("name");

                                User user = mKinveyClient.getActiveUser();
                                if (user.get("name") == null) {
                                    user.set("name", name);
                                    Person.name = name;
                                    Person.map.put("name", name);
                                }
                                if (user.get("experience") == null) {
                                    user.set("experience", 0);
                                    Person.map.put("experience", 0);
                                }
                                if (user.get("courses") == null) {
                                    user.set("courses", "");
                                    Person.map.put("courses", "");
                                }
                                if (user.get("image") == null) {
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
                                    uploadImage();
                                } else loadImage();

                                user.update(new KinveyClientCallback() {
                                    @Override
                                    public void onSuccess(Object o) {

                                    }

                                    @Override
                                    public void onFailure(Throwable throwable) {

                                    }
                                });
                                intent(true);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    void loadImage() {
        User user = mKinveyClient.getActiveUser();
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
                    FragmentProfile.d = false;
                    Toast.makeText(getApplicationContext(), "Не удалось загрузить изображение.",
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void progressChanged(MediaHttpDownloader mediaHttpDownloader) {
                }

                @Override
                public void onCancelled() {
                    FragmentProfile.d = false;
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
            FragmentProfile.d = false;
            Toast.makeText(getApplicationContext(), "Не удалось загрузить изображение.",
                    Toast.LENGTH_SHORT).show();
            e1.printStackTrace();
        }
    }

    void uploadImage() {
        FileStore fileStore = mKinveyClient.getFileStore(StoreType.CACHE);
        File file = new File(getFilesDir(), "profileImage.png");
        try {
            fileStore.upload(file, new AsyncUploaderProgressListener<FileMetaData>() {
                @Override
                public void onSuccess(FileMetaData fileMetaData) {
                    Person.id = fileMetaData.getId();
                    Person.map.put("image", Person.id);
                    Person.map.put("name", Person.name);
                    Person.map.put("experience", Person.experience);
                    Person.map.put("courses", Person.c);

                    SharedPreferences mSettings = getSharedPreferences(Person.APP_PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putString("id", Person.id);
                    editor.apply();

                    User user = mKinveyClient.getActiveUser();
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

                }

                @Override
                public void progressChanged(MediaHttpUploader mediaHttpUploader) {

                }

                @Override
                public void onCancelled() {

                }

                @Override
                public boolean isCancelled() {
                    return false;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();

            Person.name = Objects.requireNonNull(acct).getDisplayName();
            FragmentProfile.personPhotoUrl = Objects.requireNonNull(acct.getPhotoUrl()).toString();
            FragmentProfile.google = true;

            try {
                Toast.makeText(getApplicationContext(), acct.getIdToken(), Toast.LENGTH_SHORT).show();
                UserStore.loginGoogle(acct.getIdToken(), mKinveyClient, new KinveyClientCallback() {
                    @Override
                    public void onSuccess(Object o) {
                        intent(true);
                        SharedPreferences mSettings = getSharedPreferences(Person.APP_PREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putBoolean("google", true);
                        editor.apply();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Toast.makeText(getApplicationContext(), "Fail Google", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "Связь потеряна", Toast.LENGTH_SHORT).show();
    }

    void signInWithVK() {

    }

    void signInWithTwitter() {

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(i);
        finish();
    }

}




