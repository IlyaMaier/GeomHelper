package com.example.geomhelper.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.Resources.CircleImageView;
import com.kinvey.android.Client;
import com.kinvey.android.callback.AsyncUploaderProgressListener;
import com.kinvey.android.callback.KinveyDeleteCallback;
import com.kinvey.android.store.FileStore;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.core.MediaHttpUploader;
import com.kinvey.java.model.FileMetaData;
import com.kinvey.java.store.StoreType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class FragmentProfile extends Fragment {

    TextView textLevelName, textExperience, textName;
    CircleImageView circleImageView;
    Bitmap bitmap;
    ScrollView scrollView;
    Context context;
    Client mKinveyClient;
    BottomNavigationView bottomNavigationView;
    public static volatile boolean d = false;

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

        if(d) {
            Toast.makeText(getContext(), "Дождитесь окончания загрузки изображения" +
                    "", Toast.LENGTH_LONG).show();
            Async async = new Async();
            async.execute();
        }

        textName.setText(Person.name);
        textLevelName.setText(Person.currentLevel);
        textExperience.setText((Person.experience + "/" + Person.currentLevelExperience));

        try {
            try {
                bitmap = BitmapFactory.decodeFile(
                        context.getFilesDir().getPath() +
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
                                    Person.map.put("name", Person.name);
                                    Client mKinveyClient = new Client.Builder("kid_B1OS_p1hM",
                                            "602d7fccc790477ca6505a1daa3aa894",
                                            Objects.requireNonNull(getActivity()).getApplicationContext()).setBaseUrl(
                                            "https://baas.kinvey.com").build();
                                    mKinveyClient.getActiveUser().putAll(Person.map);
                                    mKinveyClient.getActiveUser().update(new KinveyClientCallback() {
                                        @Override
                                        public void onSuccess(Object o) {

                                        }

                                        @Override
                                        public void onFailure(Throwable throwable) {

                                        }
                                    });
                                    textName.setText(name);
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
                            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                                    Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        1);
                            }
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
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(
                            Person.APP_PREFERENCES, Context.MODE_PRIVATE).edit();
                    editor.apply();
                    java.io.File file = new java.io.File(Objects.requireNonNull(
                            getContext()).getFilesDir(), "profileImage.png");
                    final boolean isCancelled = false;
                    mKinveyClient = new Client.Builder("kid_B1OS_p1hM",
                            "602d7fccc790477ca6505a1daa3aa894",
                            Objects.requireNonNull(getActivity().getApplicationContext())).
                            setBaseUrl("https://baas.kinvey.com").build();
                    FileStore fileStore = mKinveyClient.getFileStore(StoreType.CACHE);
                    FileMetaData fileMetaData = new FileMetaData();
                    fileMetaData.setId(Person.id);
                    try {
                        fileStore.remove(fileMetaData, new KinveyDeleteCallback() {
                            @Override
                            public void onSuccess(Integer integer) {
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        fileStore.upload(file, new AsyncUploaderProgressListener<FileMetaData>() {
                            @Override
                            public void onSuccess(FileMetaData fileMetaData) {
                                Person.id = fileMetaData.getId();
                                Person.map.put("image", Person.id);
                                mKinveyClient.getActiveUser().putAll(Person.map);
                                mKinveyClient.getActiveUser().update(new KinveyClientCallback() {
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
                                Toast.makeText(getContext(), "Не удалось загрузить изображение.",
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
                    }
                }
        }
    }

    class Async extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            while (d) {

            }
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            try {
                try {
                    bitmap = BitmapFactory.decodeFile(
                            context.getFilesDir().getPath() +
                                    "/profileImage.png");
                    circleImageView.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

}
