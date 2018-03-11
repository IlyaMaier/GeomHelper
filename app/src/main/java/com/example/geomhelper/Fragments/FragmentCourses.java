package com.example.geomhelper.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.geomhelper.Activities.AddCourseActivity;
import com.example.geomhelper.Courses;
import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;

public class FragmentCourses extends Fragment {

    LinearLayout linearLayout;
    Button[] button;
    FloatingActionButton floatingActionButton;
    View rootView;
    int i = 0;

    public FragmentCourses() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_courses, container, false);

        linearLayout = rootView.findViewById(R.id.line1);
        button = new Button[Courses.currentCourses.size()];

        floatingActionButton = rootView.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AddCourseActivity.class);
                startActivity(i);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        if (linearLayout.getChildCount() < Person.courses.size())
            for (i = linearLayout.getChildCount(); i < Person.courses.size(); i++) {
                button[i] = new Button(getContext());
                button[i].setBackgroundResource(R.drawable.backround_button_courses);
                button[i].setText(Person.courses.get(i).getCourseName());
                button[i].setTextSize(16);
                button[i].setId(i);
                linearLayout.addView(button[i]);
                button[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.back = 1;
                        Person.backCourses = 1;
                        floatingActionButton.hide();
                        for (int j = 0; j < Person.courses.size(); j++) {
                            button[j].setEnabled(false);
                            button[j].setVisibility(View.INVISIBLE);
                        }
                        Person.currentCourse = Person.courses.get(v.getId());
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        FragmentThemes fragmentThemes = new FragmentThemes();
                        fragmentTransaction.replace(R.id.fragment, fragmentThemes);
                        fragmentTransaction.commit();
                    }
                });
            }
        super.onResume();
    }

}