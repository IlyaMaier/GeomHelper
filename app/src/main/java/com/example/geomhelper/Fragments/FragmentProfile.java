package com.example.geomhelper.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class FragmentProfile extends Fragment {

    TextView textLevelName, textExperience, textName;
    CircleImageView circleImageView;
    static ScrollView scrollView;

    public FragmentProfile() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        textName = rootView.findViewById(R.id.textName);
        textName.setText(Person.name);
        textLevelName = rootView.findViewById(R.id.textLevelName);
        textLevelName.setText(Person.currentLevel);
        textExperience = rootView.findViewById(R.id.textExperince);
        textExperience.setText((Person.experience + "/" + Person.currentLevelExperience));

        Bitmap bitmap;
        circleImageView = rootView.findViewById(R.id.imageProfile);
        if (getActivity().getSharedPreferences(Person.APP_PREFERENCES, Context.MODE_PRIVATE).
                getBoolean("image", false))
            try {
                bitmap = BitmapFactory.decodeFile("/data/data/com.example.geomhelper/files/profileImage.png");
                circleImageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        else circleImageView.setImageDrawable(getResources().getDrawable(R.drawable.back_login));
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 5);
            }
        });

        textName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(getContext());
                et.setText(Person.name);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(et)
                        .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String name = et.getText().toString();
                                if (name.contains("\n"))
                                    name = name.replaceAll("\n", "");
                                if (name.isEmpty()) {
                                    dialog.cancel();
                                } else {
                                    Person.name = name;
                                    textName.setText(name);
                                    DatabaseReference f = FirebaseDatabase.getInstance().getReference();
                                    try {
                                        f.child(FirebaseAuth.getInstance().getUid()).child("name").setValue(name);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
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

    public static void top() {
        scrollView.scrollTo(0, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 5:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    circleImageView.setImageBitmap(yourSelectedImage);
                    try {
                        File file = new File(getContext().getFilesDir(), "profileImage.png");
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
                    editor.putBoolean("image", true);
                    editor.apply();
                    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                    Uri file = Uri.fromFile(new File(
                            "/data/data/com.example.geomhelper/files/profileImage.png"));
                    try {
                        StorageReference profileRef = mStorageRef.child(FirebaseAuth.getInstance().getUid());
                        profileRef.putFile(file)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Toast.makeText(getContext(),
                                                "Не удалось загрузить изображение",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
}
