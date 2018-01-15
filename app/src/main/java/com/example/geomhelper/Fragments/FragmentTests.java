package com.example.geomhelper.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.geomhelper.Courses;
import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.Resources.Resources;

public class FragmentTests extends Fragment {

    public FragmentTests() {
    }

    Button[] button;
    LinearLayout linearLayout;
    Resources resources;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tests, container, false);

        button = new Button[Courses.currentCourses.size()];
        linearLayout = rootView.findViewById(R.id.linearLayoutFragmentTests);
        resources = new Resources();
        initializeButtons();

        return rootView;
    }

    void initializeButtons() {
        for (int i = 0; i < button.length; i++) {
            button[i] = new Button(getContext());
            button[i].setBackgroundResource(resources.background[button.length - 1 - i]);
            button[i].setText(Courses.currentCourses.get(button.length - 1 - i).getCourseName());
            button[i].setTextSize(16);
            button[i].setId(button.length - 1 - i);
            linearLayout.addView(button[i]);
            button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Person.currentTestCourse = Courses.currentCourses.get(v.getId());
                    MainActivity.back = 3;
                    Person.backTests = 3;
                    for (int j = 0; j < Courses.currentCourses.size(); j++) {
                        button[j].setEnabled(false);
                        button[j].setVisibility(View.INVISIBLE);
                    }
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    FragmentTestThemes fragmentTestThemes = new FragmentTestThemes();
                    fragmentTransaction.replace(R.id.frameTests, fragmentTestThemes);
                    fragmentTransaction.commit();
                }
            });
        }
    }
}
