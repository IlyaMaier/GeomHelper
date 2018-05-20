package com.example.geomhelper.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.geomhelper.Content.SecondTask;
import com.example.geomhelper.Content.SecondTasks;
import com.example.geomhelper.Content.Test;
import com.example.geomhelper.Content.Tests;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.geomhelper.Fragments.FragmentResult.answer2;
import static com.example.geomhelper.Person.task;

public class FragmentSecondTask extends Fragment {

    private TextView textViewTask2;
    private ImageView imageView2;
    private EditText editText2Task;
    private Button buttonEnter2;
    private SecondTask secondTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_second_task, container, false);

        initializeTests();
        initializeViews(rootView);
        setInfo();

        buttonEnter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer2 = editText2Task.getText().toString();
                FragmentThirdTask fragmentThirdTask = new FragmentThirdTask();
                FragmentManager fragmentManager = getFragmentManager();
                Objects.requireNonNull(fragmentManager).beginTransaction().replace(
                        R.id.frame_tests, fragmentThirdTask).addToBackStack(null).commit();
            }
        });
        return rootView;
    }

    private void initializeTests() {
        List<Test> tests = new Tests().getCurrentTests();
        ArrayList<SecondTask> secondTasks = new SecondTasks().
                getTasks(tests.size() - 1 - Person.currentTest, Person.currentTestTheme);
        secondTask = secondTasks.get(task);
    }

    private void initializeViews(View rootView) {
        textViewTask2 = rootView.findViewById(R.id.textViewTask2);

        imageView2 = rootView.findViewById(R.id.imageView2);

        editText2Task = rootView.findViewById(R.id.editText2Task);

        buttonEnter2 = rootView.findViewById(R.id.buttonEnter2);
    }

    private void setInfo() {
        textViewTask2.setText(secondTask.getTextViewTask2());
        imageView2.setImageDrawable(getResources()
                .getDrawable(secondTask.getImageView2()));
    }

}