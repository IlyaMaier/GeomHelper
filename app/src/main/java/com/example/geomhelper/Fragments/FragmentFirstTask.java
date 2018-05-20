package com.example.geomhelper.Fragments;

import android.os.Bundle;
import androidx.annotation.FractionRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geomhelper.Content.FirstTask;
import com.example.geomhelper.Content.FirstTasks;
import com.example.geomhelper.Content.Test;
import com.example.geomhelper.Content.Tests;
import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.TestJSON;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.geomhelper.Fragments.FragmentResult.answer1;
import static com.example.geomhelper.Person.pref;
import static com.example.geomhelper.Person.task;

public class FragmentFirstTask extends Fragment {

    TextView textViewName1, textViewTask1;
    ImageView imageView1;
    RadioButton radioButton, radioButton1, radioButton2;
    Button buttonEnter1;
    List<Test> tests;
    List<FirstTask> firstTasks;
    FirstTask firstTask;
    boolean t = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first_task, container, false);

        initializeTests();
        initializeViews(rootView);
        setInfo();

        buttonEnter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButton.isChecked())
                    answer1 = 0;
                else if (radioButton1.isChecked())
                    answer1 = 1;
                else if (radioButton2.isChecked())
                    answer1 = 2;

                FragmentSecondTask fragmentSecondTask = new FragmentSecondTask();
                FragmentManager fragmentManager = getFragmentManager();
                Objects.requireNonNull(fragmentManager).beginTransaction()
                        .replace(R.id.frame_tests, fragmentSecondTask).addToBackStack(null).commit();
            }
        });

        return rootView;
    }

    void initializeTests() {
        tests = new Tests().getCurrentTests();

        firstTasks = new FirstTasks().getTasks(Person.currentTest, Person.currentTestTheme);
        TestJSON testJSON = new Gson().fromJson(
                pref.getString("tests", null), TestJSON.class);
        if (testJSON == null) {
            testJSON = new TestJSON();
            testJSON.setTest(Person.currentTest, Person.currentTestTheme, 0);
        } else task = testJSON.getTest(Person.currentTest, Person.currentTestTheme);

        ArrayList<FirstTask> firstTasks = new FirstTasks().
                getTasks(tests.size() - 1 - Person.currentTest, Person.currentTestTheme);
        if (firstTasks.size() == 0||task==firstTasks.size()) {
            t = false;
            Toast.makeText(getContext(), "Тесты по данной теме закончились или недоступны",
                    Toast.LENGTH_SHORT).show();
            close();
        } else firstTask = firstTasks.get(task);
    }

    void initializeViews(View rootView) {
        textViewName1 = rootView.findViewById(R.id.textViewName1);
        textViewTask1 = rootView.findViewById(R.id.textViewTask1);

        imageView1 = rootView.findViewById(R.id.imageView1);

        radioButton = rootView.findViewById(R.id.radioButton);
        radioButton1 = rootView.findViewById(R.id.radioButton1);
        radioButton2 = rootView.findViewById(R.id.radioButton2);

        buttonEnter1 = rootView.findViewById(R.id.buttonEnter1);
    }

    void setInfo() {
        if (t) {
            textViewTask1.setText(firstTask.getTextViewTask1());
            imageView1.setImageDrawable(getResources().
                    getDrawable(firstTask.getImageView1()));
            radioButton.setText(firstTask.getRadioButton());
            radioButton1.setText(firstTask.getRadioButton1());
            radioButton2.setText(firstTask.getRadioButton2());
        }
    }

    void close() {
        MainActivity.back = 3;
        Person.backTests = 3;
        FragmentTransaction fragmentTransaction =
                Objects.requireNonNull(getFragmentManager()).beginTransaction();
        FragmentTestThemes fragmentTestThemes = new FragmentTestThemes();
        fragmentTransaction.replace(R.id.frame_tests, fragmentTestThemes);
        fragmentTransaction.commit();
    }

}
