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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Objects;
import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static com.example.geomhelper.Person.pref;

public class FragmentProfile extends Fragment {

    TextView textLevelName, textExperience, textName;
    CircleImageView circleImageView;
    Bitmap bitmap;
    ScrollView scrollView;
    Context context;
    boolean count = false;

    public FragmentProfile() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        context = getContext();

        textName = rootView.findViewById(R.id.textName);
        textLevelName = rootView.findViewById(R.id.textLevelName);
        textExperience = rootView.findViewById(R.id.textExperince);
        circleImageView = rootView.findViewById(R.id.imageProfile);

        textName.setText(Person.name);
        textLevelName.setText(Person.currentLevel);
        textExperience.setText((Person.experience + "/" + Person.currentLevelExperience));

        try {
            if (pref.getBoolean("image", false))
                try {
                    bitmap = BitmapFactory.decodeFile(
                            context.getFilesDir().getPath() +
                                    "/profileImage.png");
                    circleImageView.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            else
                circleImageView.setImageDrawable(getResources().getDrawable(R.drawable.back_login));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
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
                                    Person.name = name;
                                    textName.setText(name);
                                    DatabaseReference f = FirebaseDatabase.getInstance().getReference();
                                    try {
                                        f.child(Person.uId).child("name").setValue(name);
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

        if (Person.pref.getBoolean(Person.APP_PREFERENCES_WELCOME, false)) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child(Person.uId).child("name").
                    addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Person.name = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                            textName.setText(Person.name);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            databaseReference.child(Person.uId).child("image").
                    addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (count) {
                                try {
                                    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                                    Uri file = Uri.fromFile(new File(
                                            context.getFilesDir().getPath() +
                                                    "/profileImage.png"));
                                    mStorageRef.child(Person.uId).getFile(file);
                                    bitmap = BitmapFactory.decodeFile(
                                            context.getFilesDir().getPath() +
                                                    "/profileImage.png");
                                    circleImageView.setImageBitmap(bitmap);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else count = true;
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

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
            case 5:
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
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(
                            Person.APP_PREFERENCES, Context.MODE_PRIVATE).edit();
                    editor.putBoolean("image", true);
                    editor.apply();
                    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                    Uri file = Uri.fromFile(new File(
                            context.getFilesDir().getPath() +
                                    "/profileImage.png"));
                    try {
                        StorageReference profileRef = mStorageRef.child(Person.uId);
                        profileRef.putFile(file)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Toast.makeText(context,
                                                "Не удалось загрузить изображение",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        FirebaseDatabase.getInstance().getReference()
                                                .child(Person.uId).child("image")
                                                .setValue(new Random().nextInt());
                                    }
                                });
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
}
