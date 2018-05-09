package com.example.geomhelper.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.Resources.CircleImageView;
import com.example.geomhelper.User;
import com.example.geomhelper.UserService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.app.Activity.RESULT_OK;

public class FragmentProfile extends Fragment {

    TextView textLevelName, textExperience, textName;
    CircleImageView circleImageView;
    Bitmap bitmap;
    ScrollView scrollView;
    Context context;
    BottomNavigationView bottomNavigationView;
    public static volatile boolean d = false, a = false, google = false;
    public static String personPhotoUrl = "";

    public FragmentProfile() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        context = getContext();

        bottomNavigationView = Objects.requireNonNull(getActivity())
                .findViewById(R.id.navigation);
        textName = rootView.findViewById(R.id.textName);
        textLevelName = rootView.findViewById(R.id.textLevelName);
        textExperience = rootView.findViewById(R.id.textExperince);
        circleImageView = rootView.findViewById(R.id.imageProfile);

        if (google) {
            Glide.with(getContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(circleImageView);

            Bitmap bitmap = circleImageView.getDrawingCache();
            File file = new File(context.getFilesDir(), "profileImage.png");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        textName.setText(Person.name);
        textLevelName.setText(Person.currentLevel);
        textExperience.setText((Person.experience + "/" + Person.currentLevelExperience));

        try {
            try {
                bitmap = BitmapFactory.decodeFile(
                        context.getFilesDir() +
                                "/profileImage.png");

                circleImageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                }
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 500);
            }
        });

        textName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(context);
                et.setText(Person.name);
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(et)
                        .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String name = et.getText().toString();
                                if (name.contains("\n"))
                                    name = name.replaceAll("\n", "");
                                if (name.isEmpty()) {
                                    dialog.cancel();
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
                                                        Toast.makeText(context, "Не удалось отправить имя на сервер",
                                                                Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                                    Toast.makeText(context, "Не удалось отправить имя на сервер",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    textName.setText(Person.name);
                                }
                            }
                        }).setNegativeButton("ОТМЕНИТЬ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog ad = builder.create();
                ad.show();
            }
        });
        scrollView = rootView.findViewById(R.id.scroll_profile);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        textName.setText(Person.name);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 500:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    InputStream imageStream = null;
                    try {
                        if (selectedImage != null) {
                            imageStream = Objects.requireNonNull(getActivity())
                                    .getContentResolver().openInputStream(selectedImage);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    circleImageView.setImageBitmap(yourSelectedImage);
                    try {
                        File file = new File(context.getFilesDir(), "profileImage.png");
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
                }
        }
    }

}
