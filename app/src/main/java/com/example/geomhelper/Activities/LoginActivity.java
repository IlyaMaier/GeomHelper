package com.example.geomhelper.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geomhelper.Content.Course;
import com.example.geomhelper.Content.Courses;
import com.example.geomhelper.Content.Levels;
import com.example.geomhelper.Fragments.FragmentProfile;
import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.User;
import com.example.geomhelper.UserService;
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
import com.google.gson.Gson;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.util.VKUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private final int RC_SIGN_IN = 9007;
    private EditText mEmail;
    private EditText mPassword;
    private GoogleApiClient mGoogleApiClient;
    private String tests;

    Retrofit retrofit;
    UserService userService;
    CallbackManager callbackManager;
    ProgressDialog progressDialog;
    LinearLayout linearLayout;

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

        //facebook
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getMeInfo();
            }

            @Override
            public void onCancel() {
                progressDialog.cancel();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(),
                        "Произошла ошибка при попытке входа через Facebook",
                        Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
            }
        });

        //google+
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(User.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);

        VKSdk.initialize(getApplicationContext());
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
        }
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Загрузка...");
        progressDialog.show();
    }

    @SuppressLint("StaticFieldLeak")
    void signIn() {
        if (validateForm()) {
            userService.login(mEmail.getText().toString(),
                    User.md5(mPassword.getText().toString()))
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                            switch (Objects.requireNonNull(response.body())) {
                                case "0":
                                    Toast.makeText(getApplicationContext(), "Произошла ошибка!",
                                            Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();
                                    break;
                                case "2":
                                    Toast.makeText(getApplicationContext(),
                                            "Такого пользователя не существует!",
                                            Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();
                                    break;
                                case "3":
                                    Toast.makeText(getApplicationContext(), "Неверный пароль!",
                                            Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();
                                    break;
                                default:
                                    User user = new Gson().fromJson(response.body(), User.class);
                                    Person.id = String.valueOf(user.getId());
                                    Person.name = user.getName();
                                    Person.experience = user.getExperience();
                                    Person.currentLevel = new Levels().getLevel(Person.experience);
                                    Person.currentLevelExperience = new Levels().getLevelExperience(Person.currentLevel);
                                    Person.c = user.getCourses();
                                    tests = user.getTests();

                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(User.URL)
                                            .addConverterFactory(ScalarsConverterFactory.create())
                                            .build();

                                    UserService userService = retrofit.create(UserService.class);
                                    userService.downloadimage(Person.id).enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(@NonNull Call<String> call,
                                                               @NonNull Response<String> response) {
                                            try {
                                                String[] strings = Objects.requireNonNull(response.body()).split(",");
                                                byte[] w = new byte[strings.length];
                                                for (int i = 0; i < w.length; i++)
                                                    w[i] = Byte.parseByte(strings[i]);

                                                Bitmap bitmap = BitmapFactory.decodeByteArray(w, 0, w.length);
                                                try {
                                                    File file = new File(getApplicationContext().getFilesDir(), "profileImage.png");
                                                    FileOutputStream fos = null;
                                                    try {
                                                        fos = new FileOutputStream(file);
                                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                                    } finally {
                                                        if (fos != null) fos.close();
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            } catch (NullPointerException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                            Toast.makeText(LoginActivity.this,
                                                    "Не удалось загрузить изображение", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    List<Course> courses = new Courses().getCurrentCourses();
                                    if (Person.c != null && !Person.c.isEmpty())
                                        for (int i = 0; i < Person.c.length(); i++)
                                            Person.courses.add(0, courses.get(
                                                    Integer.parseInt(Person.c.charAt(i) + "")));

                                    saveAndFinish();
                                    break;
                            }
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

    void saveAndFinish() {
        SharedPreferences mSettings = getSharedPreferences(Person.APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(Person.APP_PREFERENCES_WELCOME, true);
        editor.putBoolean("image", true);
        editor.putString(Person.APP_PREFERENCES_UID, Person.id);
        editor.putString(Person.APP_PREFERENCES_NAME, Person.name);
        editor.putString("c", Person.c);
        editor.putString("tests", tests);
        editor.putInt("experience", Person.experience);
        editor.putString(Person.APP_PREFERENCES_LEVEL, Person.currentLevel);
        editor.putInt(Person.APP_PREFERENCES_LEVEL_EXPERIENCE, Person.currentLevelExperience);
        editor.apply();

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();

        CharSequence text;
        if (Person.name != null)
            text = "Добро пожаловать , " + Person.name + "!";
        else text = "Добро пожаловать!";
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
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(final VKAccessToken res) {
                final String email = res.email;
                if (email == null) {
                    Toast.makeText(LoginActivity.this,
                            "Произошла ошибка при входе через VK",
                            Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                } else {
                    VKApi.users().get(VKParameters.from(VKApiConst.USER_ID, res.userId))
                            .executeWithListener(new VKRequest.VKRequestListener() {
                                @Override
                                public void onComplete(VKResponse response) {
                                    try {
                                        final String name = response.json.getJSONArray("response").getJSONObject(0)
                                                .getString("first_name") + " " +
                                                response.json.getJSONArray("response").getJSONObject(0)
                                                        .getString("last_name");
                                        userService.loginWithSocial(email, User.md5(getString(R.string.soc)), name)
                                                .enqueue(new Callback<String>() {
                                                    @Override
                                                    public void onResponse(@NonNull Call<String> call,
                                                                           @NonNull Response<String> response) {
                                                        if (Objects.requireNonNull(response.body()).equals("0")) {
                                                            Toast.makeText(LoginActivity.this,
                                                                    "Произошла ошибка при входе через VK",
                                                                    Toast.LENGTH_SHORT).show();
                                                            progressDialog.cancel();
                                                        } else {
                                                            if (Objects.requireNonNull(response.body()).length() > 10) {
                                                                User user = new Gson().fromJson(response.body(), User.class);
                                                                Person.id = String.valueOf(user.getId());
                                                                Person.name = user.getName();
                                                                Person.experience = user.getExperience();
                                                                Person.currentLevel = new Levels().getLevel(Person.experience);
                                                                Person.currentLevelExperience = new Levels().getLevelExperience(Person.currentLevel);
                                                                Person.c = user.getCourses();
                                                                tests = user.getTests();

                                                                List<Course> courses = new Courses().getCurrentCourses();
                                                                if (Person.c != null && !Person.c.isEmpty())
                                                                    for (int i = 0; i < Person.c.length(); i++)
                                                                        Person.courses.add(0, courses.get(
                                                                                Integer.parseInt(Person.c.charAt(i) + "")));
                                                            } else {
                                                                Person.name = name;
                                                                Person.id = response.body();
                                                            }
                                                            VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo"));
                                                            request.executeWithListener(new VKRequest.VKRequestListener() {
                                                                @Override
                                                                public void onComplete(VKResponse response) {
                                                                    try {
                                                                        FragmentProfile.personPhotoUrl = response.json
                                                                                .getJSONArray("response").getJSONObject(0)
                                                                                .getString("photo");
                                                                    } catch (JSONException e) {
                                                                        Toast.makeText(LoginActivity.this,
                                                                                "Не удалось загрузить картинку!",
                                                                                Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }

                                                                @Override
                                                                public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                                                                    Toast.makeText(LoginActivity.this,
                                                                            "Не удалось загрузить картинку!",
                                                                            Toast.LENGTH_SHORT).show();
                                                                }

                                                                @Override
                                                                public void onError(VKError error) {
                                                                    Toast.makeText(LoginActivity.this,
                                                                            "Не удалось загрузить картинку!",
                                                                            Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                            FragmentProfile.social = true;
                                                            saveAndFinish();
                                                            getSharedPreferences(Person.APP_PREFERENCES, MODE_PRIVATE)
                                                                    .edit().putBoolean("vk", true).apply();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                                        Toast.makeText(LoginActivity.this,
                                                                "Произошла ошибка при входе через VK",
                                                                Toast.LENGTH_SHORT).show();
                                                        progressDialog.cancel();
                                                    }
                                                });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(LoginActivity.this,
                                                "Произошла ошибка при входе через VK",
                                                Toast.LENGTH_SHORT).show();
                                        progressDialog.cancel();
                                    }
                                }

                                @Override
                                public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                                    Toast.makeText(getApplicationContext(),
                                            "Произошла ошибка при входе через VK!", Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();
                                }

                                @Override
                                public void onError(VKError error) {
                                    Toast.makeText(getApplicationContext(),
                                            "Произошла ошибка при входе через VK!", Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();
                                }
                            });
                }
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(),
                        "Произошла ошибка при входе через VK!", Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
            }
        })) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        }
    }

    public void getMeInfo() {
        AccessToken token = AccessToken.getCurrentAccessToken();

        final GraphRequest request = GraphRequest.newGraphPathRequest(
                token,
                "me",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        JSONObject object;
                        try {
                            if (response.getError() == null) {
                                object = new JSONObject(response.getRawResponse());
                                final String name = object.getString("name");
                                String email = object.getString("email");
                                final String photo = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                userService.loginWithSocial(email, User.md5(getString(R.string.soc)), name)
                                        .enqueue(new Callback<String>() {
                                            @Override
                                            public void onResponse(@NonNull Call<String> call,
                                                                   @NonNull Response<String> response) {
                                                if (Objects.requireNonNull(response.body()).equals("0")) {
                                                    Toast.makeText(LoginActivity.this,
                                                            "Произошла ошибка при входе через Facebook",
                                                            Toast.LENGTH_SHORT).show();
                                                    progressDialog.cancel();
                                                } else {
                                                    if (Objects.requireNonNull(response.body()).length() > 10) {
                                                        User user = new Gson().fromJson(response.body(), User.class);
                                                        Person.id = String.valueOf(user.getId());
                                                        Person.name = user.getName();
                                                        Person.experience = user.getExperience();
                                                        Person.currentLevel = new Levels().getLevel(Person.experience);
                                                        Person.currentLevelExperience = new Levels().getLevelExperience(Person.currentLevel);
                                                        Person.c = user.getCourses();
                                                        tests = user.getTests();

                                                        List<Course> courses = new Courses().getCurrentCourses();
                                                        if (Person.c != null && !Person.c.isEmpty())
                                                            for (int i = 0; i < Person.c.length(); i++)
                                                                Person.courses.add(0, courses.get(
                                                                        Integer.parseInt(Person.c.charAt(i) + "")));
                                                    } else {
                                                        Person.name = name;
                                                        Person.id = response.body();
                                                    }
                                                    FragmentProfile.personPhotoUrl = photo;
                                                    FragmentProfile.social = true;
                                                    saveAndFinish();
                                                    getSharedPreferences(Person.APP_PREFERENCES, MODE_PRIVATE)
                                                            .edit().putBoolean("facebook", true).apply();
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                                Toast.makeText(LoginActivity.this,
                                                        "Произошла ошибка при входе через Facebook",
                                                        Toast.LENGTH_SHORT).show();
                                                progressDialog.cancel();
                                            }
                                        });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this,
                                    "Произошла ошибка при входе через Facebook",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
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
            FragmentProfile.social = true;

            userService.loginWithSocial(acct.getEmail(), User.md5(getString(R.string.soc)), Person.name)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call,
                                               @NonNull Response<String> response) {
                            if (Objects.requireNonNull(response.body()).equals("0")) {
                                Toast.makeText(LoginActivity.this,
                                        "Произошла ошибка при входе через Google",
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            } else {
                                if (Objects.requireNonNull(response.body()).length() > 10) {
                                    User user = new Gson().fromJson(response.body(), User.class);
                                    Person.id = String.valueOf(user.getId());
                                    Person.name = user.getName();
                                    Person.experience = user.getExperience();
                                    Person.currentLevel = new Levels().getLevel(Person.experience);
                                    Person.currentLevelExperience = new Levels().getLevelExperience(Person.currentLevel);
                                    Person.c = user.getCourses();
                                    tests = user.getTests();

                                    List<Course> courses = new Courses().getCurrentCourses();
                                    if (Person.c != null && !Person.c.isEmpty())
                                        for (int i = 0; i < Person.c.length(); i++)
                                            Person.courses.add(0, courses.get(
                                                    Integer.parseInt(Person.c.charAt(i) + "")));
                                } else {
                                    Person.id = response.body();
                                }
                                getSharedPreferences(Person.APP_PREFERENCES, MODE_PRIVATE)
                                        .edit().putBoolean("google", true).apply();
                                saveAndFinish();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                            Toast.makeText(LoginActivity.this,
                                    "Произошла ошибка при входе через Google",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "Связь потеряна", Toast.LENGTH_SHORT).show();
    }

    void signInWithVK() {
        VKSdk.login(this, "email");
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(i);
        finish();
    }

}
