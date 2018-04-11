package com.example.geomhelper.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.Resources.Resources;

import java.util.Objects;

public class FragmentThemes extends Fragment {

    public FragmentThemes() {
    }

    Resources resources = new Resources();
    Button[] button;
    LinearLayout linearLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_themes, container, false);

        linearLayout = view.findViewById(R.id.linearLayoutFragmentThemes);
        button = new Button[Person.currentCourse.getNumberOfThemes()];

        for (int i = 0; i < button.length; i++) {
            button[i] = new Button(getContext());
            button[i].setHeight(250);
            button[i].setBackgroundColor(resources.colors[i]);
            button[i].setText(Person.currentCourse.getTheme(i));
            button[i].setTextSize(16);
            button[i].setId(i);
            linearLayout.addView(button[i]);
            button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.back = 2;
                    Person.backCourses = 2;
                    Person.currentTheme = v.getId();
                    FragmentTransaction fragmentTransaction = Objects.
                            requireNonNull(getFragmentManager()).beginTransaction();
                    FragmentCourseText fragmentCourseText = new FragmentCourseText();
                    fragmentTransaction.replace(R.id.fragment, fragmentCourseText);
                    fragmentTransaction.commit();
                }
            });
        }
        return view;
    }

}
