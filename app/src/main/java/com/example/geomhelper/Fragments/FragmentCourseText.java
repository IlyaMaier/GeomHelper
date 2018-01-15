package com.example.geomhelper.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;

public class FragmentCourseText extends Fragment {

    public FragmentCourseText() {
    }

    TextView textView;
    FloatingActionButton floatingActionButton, floatingActionButton2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_text, container, false);
        view.setBackgroundColor(getResources().getColor(R.color.white));

        textView = view.findViewById(R.id.textViewFragmentCourseText);
        textView.setText(Person.currentCourse.getThemesText(Person.currentTheme));

        floatingActionButton2 = view.findViewById(R.id.floatingActionButton3);
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Person.currentTheme == 0) {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    FragmentThemes fragmentThemes = new FragmentThemes();
                    fragmentTransaction.replace(R.id.fragment, fragmentThemes);
                    fragmentTransaction.commit();
                    MainActivity.back = 1;
                    Person.backCourses = 1;
                } else {
                    floatingActionButton.setImageResource(R.drawable.ic_next);
                    Person.currentTheme--;
                    textView.setText(Person.currentCourse.getThemesText(Person.currentTheme));
                }
            }
        });

        floatingActionButton = view.findViewById(R.id.floatingActionButton2);
        if (Person.currentCourse.getThemesSize() - 1 == Person.currentTheme)
            floatingActionButton.setImageResource(R.drawable.ic_completed);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Person.currentCourse.getThemesSize() - 1 == Person.currentTheme) {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    FragmentThemes fragmentThemes = new FragmentThemes();
                    fragmentTransaction.replace(R.id.fragment, fragmentThemes);
                    fragmentTransaction.commit();
                    MainActivity.back = 1;
                    Person.backCourses = 1;
                } else {
                    Person.currentTheme++;
                    textView.setText(Person.currentCourse.getThemesText(Person.currentTheme));
                    if (Person.currentCourse.getThemesSize() - 1 == Person.currentTheme)
                        floatingActionButton.setImageResource(R.drawable.ic_completed);
                }
            }
        });
        return view;
    }

}
