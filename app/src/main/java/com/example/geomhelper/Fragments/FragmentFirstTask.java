package com.example.geomhelper.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geomhelper.Content.Tests;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.User;
import com.example.geomhelper.UserService;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.example.geomhelper.Content.FirstTasks.tasks;

public class FragmentFirstTask extends Fragment {

    TextView textViewName1, textViewTask1;
    ImageView imageView1;
    RadioButton radioButton, radioButton1, radioButton2;
    Button buttonEnter1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first_task, container, false);

        textViewName1 = rootView.findViewById(R.id.textViewName1);
        textViewTask1 = rootView.findViewById(R.id.textViewTask1);

        imageView1 = rootView.findViewById(R.id.imageView1);

        radioButton = rootView.findViewById(R.id.radioButton);
        radioButton1 = rootView.findViewById(R.id.radioButton1);
        radioButton2 = rootView.findViewById(R.id.radioButton2);

        buttonEnter1 = rootView.findViewById(R.id.buttonEnter1);
        buttonEnter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Person.currentTestTheme == 0 && Person.currentTest == Tests.currentTests.get(3)) {
                    switch (tasks.get(0).getCorrectAnswer()) {
                        case 0:
                            if (radioButton.isChecked())
                                Person.experience += tasks.get(0).getExperience();
                            break;
                        case 1:
                            if (radioButton1.isChecked())
                                Person.experience += tasks.get(0).getExperience();
                            break;
                        case 2:
                            if (radioButton2.isChecked())
                                Person.experience += tasks.get(0).getExperience();
                            break;
                    }
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(User.URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .build();

                    try {
                        UserService userService = retrofit.create(UserService.class);
                        userService.updateUser(Person.id,"experience", String.valueOf(Person.experience))
                                .enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                        Toast.makeText(getContext(),
                                                "Не удалось отправить данные на сервер", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } catch (NullPointerException e) {
                        Toast.makeText(getContext(),
                                "Не удалось отправить данные на сервер", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                FragmentSecondTask fragmentSecondTask = new FragmentSecondTask();
                FragmentManager fragmentManager = getFragmentManager();
                Objects.requireNonNull(fragmentManager).beginTransaction()
                        .replace(R.id.frame_tests, fragmentSecondTask).addToBackStack(null).commit();
            }
        });

        if(Person.currentTestTheme == 0 && Person.currentTest == Tests.currentTests.get(3)){
            textViewTask1.setText(tasks.get(0).getTextViewTask1());
            imageView1.setImageDrawable(getResources().getDrawable(tasks.get(0).getImageView1()));
            radioButton.setText(tasks.get(0).getRadioButton());
            radioButton1.setText(tasks.get(0).getRadioButton1());
            radioButton2.setText(tasks.get(0).getRadioButton2());
        }
        return rootView;
    }
}