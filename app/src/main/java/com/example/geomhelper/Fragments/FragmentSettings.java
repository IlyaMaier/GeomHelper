package com.example.geomhelper.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.geomhelper.Activities.LoginActivity;
import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.User;
import com.example.geomhelper.UserService;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.example.geomhelper.Person.pref;

public class FragmentSettings extends PreferenceFragmentCompat
        implements GoogleApiClient.OnConnectionFailedListener {

    private EditTextPreference editTextPreferenceName;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor editor;
    private Activity mCurrentActivity;
    private AlertDialog.Builder builder;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        mCurrentActivity = getActivity();
        setPreferencesFromResource(R.xml.preferences, rootKey);

        try {
            mSettings = mCurrentActivity.getSharedPreferences(Person.APP_PREFERENCES, Context.MODE_PRIVATE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        editTextPreferenceName = (EditTextPreference) findPreference("name");
        editTextPreferenceName.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                editTextPreferenceName.setText(Person.name);
                return false;
            }
        });

        editTextPreferenceName.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String name = newValue.toString();
                if (name.contains("\n"))
                    name = name.replaceAll("\n", "");
                if (newValue.toString().isEmpty()) {
                    return false;
                } else {
                    if (name.length() > 20) name = name.substring(0, 20);
                    Person.name = name;

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(User.URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .build();
                    UserService userService = retrofit.create(UserService.class);
                    userService.updateUser(Person.id, "name", Person.name)
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                    if (Objects.requireNonNull(response.body()).equals("0"))
                                        Toast.makeText(getContext(), "Не удалось отправить имя на сервер",
                                                Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                    Toast.makeText(getContext(), "Не удалось отправить имя на сервер",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    return true;
                }
            }
        });

        ListPreference listPreference = (ListPreference) findPreference("pref_day_night");
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                editor = mSettings.edit();
                if (newValue.equals("Включен")) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    try {
                        mCurrentActivity.finish();
                        mCurrentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        mCurrentActivity.startActivity(mCurrentActivity.getIntent());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    editor.putString("pref_day_night", "Включен");
                } else if (newValue.equals("Выключен")) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    try {
                        mCurrentActivity.finish();
                        mCurrentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        mCurrentActivity.startActivity(mCurrentActivity.getIntent());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    editor.putString("pref_day_night", "Выключен");
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
                    try {
                        mCurrentActivity.finish();
                        mCurrentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        mCurrentActivity.startActivity(mCurrentActivity.getIntent());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    editor.putString("pref_day_night", "Авто");
                }
                editor.putBoolean("fragment_settings", true);
                editor.apply();
                return true;
            }
        });

        Preference share = findPreference("share");
        share.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT,
                        "Скачай приложение GeomHelper! Оно поможет тебе в изучении геометрии по школьной программе!" +
                                "https://yadi.sk/d/ub5kRUYy3SSHWC");
                i.setType("text/plain");
                startActivity(Intent.createChooser(i, "Поделиться"));
                return false;
            }
        });

        Preference news = findPreference("news");
        news.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Что нового");
                builder.setMessage("Добавлен стартовый экран.\n" +
                        "Добавлена возможность входа через социальные сети.\n" +
                        "Добавлены тесты, а также переписан полностью сервер.\n" +
                        "Добавлено множество анимаций.\n" +
                        "Добавлена возможность смены email и пароля.\n" +
                        "Обновлены разделы в настройках.\n");
                builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return false;
            }
        });

        Preference aboutAuthors = findPreference("about_authors");
        aboutAuthors.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Об авторах");
                builder.setMessage("Проект написан Ильей Майером и Мирославой Чобитько.\n" +
                        "Отдельная благодарность Ямалову Роману!");
                builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return false;
            }
        });

        Preference about = findPreference("about");
        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("О приложении");
                builder.setMessage("У многих школьников возникают проблемы с изучением такого непростого предмета, как геометрия.\n" +
                        "\n" +
                        "Идея нашего проекта заключается в создании простого в понимании интерактивного учебника с разнообразными тестами и заданиями, который поможет решить эту проблему. \n" +
                        "\n" +
                        "Так же мы хотим реализовать турнирную таблицу, чтобы каждый пользователь мог наблюдать не только за своими успехами, но и за успехами других людей.\n");
                builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return false;
            }
        });

        Preference acc = findPreference("acc");
        acc.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Выход из аккаунта");
                builder.setMessage("Вы действительно хотите выйти из аккаунта?");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        Person.name = "";
                        Person.id = "";
                        Person.courses.clear();
                        Person.experience = 0;
                        Person.leaderBoardPlace = 0;
                        Person.c = "";

                        pref.edit().putString("tests", "").putString("pref_day_night", "Выключен").apply();

                        if (pref.getBoolean("facebook", false)) {
                            LoginManager.getInstance().logOut();
                            pref.edit().putBoolean("facebook", false).apply();
                        }
                        if (pref.getBoolean("google", false)) {
                            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                            pref.edit().putBoolean("google", false).apply();
                        }
                        pref.edit().putBoolean("vk", false).apply();
                        MainActivity.saveAll(true, false);
                        Intent i = new Intent(getContext(), LoginActivity.class);
                        startActivity(i);
                    }
                });
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setCancelable(true);
                builder.show();
                return false;
            }
        });


        Preference email = findPreference("email");
        email.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (pref.getBoolean("facebook", false)
                        || pref.getBoolean("google", false)
                        || pref.getBoolean("vk", false)) {
                    Toast.makeText(getContext(),
                            "Нельзя изменить Email при входе через социальные сети.",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                builder = new AlertDialog.Builder(getContext());
                final LayoutInflater factory = LayoutInflater.from(getContext());
                final View textEntryView = factory.inflate(R.layout.alert_email, null);

                final EditText input1 = textEntryView.findViewById(R.id.et_email1);
                final EditText input2 = textEntryView.findViewById(R.id.et_email2);

                builder.setTitle("Смена Email");
                builder.setView(textEntryView);
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int arg1) {
                        if (input2.getText() == null || input2.getText().toString().isEmpty()) {
                            Toast.makeText(getContext(), "Вы ввели пустой Email",
                                    Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                            return;
                        }

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(User.URL)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .build();
                        UserService userService = retrofit.create(UserService.class);
                        userService.changeEmail(Person.id, User.md5(input1.getText().toString())
                                , input2.getText().toString())
                                .enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(@NonNull Call<String> call,
                                                           @NonNull Response<String> response) {
                                        String r = response.body();
                                        if (Objects.requireNonNull(r).equals("0"))
                                            Toast.makeText(getContext(), "Произошла ошибка на сервере!",
                                                    Toast.LENGTH_SHORT).show();
                                        else if (r.equals("2")) {
                                            Toast.makeText(getContext(), "Неправильный пароль!",
                                                    Toast.LENGTH_SHORT).show();
                                        } else if (r.equals("3")) {
                                            Toast.makeText(getContext(), "Данный Email уже занят.",
                                                    Toast.LENGTH_SHORT).show();
                                        } else if (r.equals("1")) {
                                            Toast.makeText(getContext(), "Email изменен!",
                                                    Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                        dialog.cancel();
                                    }
                                });
                    }
                });
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setCancelable(false);
                builder.show();

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) textEntryView.getLayoutParams();
                params.setMargins(24, 24, 24, 24);

                return false;
            }
        });

        Preference password = findPreference("password");
        password.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (pref.getBoolean("facebook", false)
                        || pref.getBoolean("google", false)
                        || pref.getBoolean("vk", false)) {
                    Toast.makeText(getContext(),
                            "Нельзя изменить пароль при входе через социальные сети.",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                builder = new AlertDialog.Builder(getContext());
                LayoutInflater factory = LayoutInflater.from(getContext());
                final View textEntryView = factory.inflate(R.layout.alert_password, null);

                final EditText input1 = textEntryView.findViewById(R.id.et_password1);
                final EditText input2 = textEntryView.findViewById(R.id.et_password2);
                final EditText input3 = textEntryView.findViewById(R.id.et_password3);

                builder.setTitle("Смена пароля");
                builder.setView(textEntryView);
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int arg1) {
                        if (input1.getText() == null || input1.getText().toString().isEmpty()
                                || input2.getText() == null || input2.getText().toString().isEmpty()
                                || input3.getText() == null || input3.getText().toString().isEmpty()) {
                            Toast.makeText(getContext(), "Вы ввели пустой пароль",
                                    Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                            return;
                        }

                        if (!input2.getText().toString().equals(input3.getText().toString())) {
                            Toast.makeText(getContext(), "Пароли не совпадают",
                                    Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                            return;
                        }

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(User.URL)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .build();
                        UserService userService = retrofit.create(UserService.class);
                        userService.changePassword(Person.id, User.md5(input1.getText().toString())
                                , User.md5(input2.getText().toString()))
                                .enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(@NonNull Call<String> call,
                                                           @NonNull Response<String> response) {
                                        String r = response.body();
                                        if (Objects.requireNonNull(r).equals("0"))
                                            Toast.makeText(getContext(), "Произошла ошибка на сервере!",
                                                    Toast.LENGTH_SHORT).show();
                                        else if (r.equals("2")) {
                                            Toast.makeText(getContext(), "Неправильный пароль!",
                                                    Toast.LENGTH_SHORT).show();
                                        } else if (r.equals("1")) {
                                            Toast.makeText(getContext(), "Пароль изменен!",
                                                    Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                        dialog.cancel();
                                    }
                                });
                    }
                });
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setCancelable(false);
                builder.show();

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) textEntryView.getLayoutParams();
                params.setMargins(24, 24, 24, 24);

                return false;
            }
        });

    }


    @Override
    public void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(Objects.requireNonNull(getContext()))
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), "Связь потеряна", Toast.LENGTH_SHORT).show();
    }

}
