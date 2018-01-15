package com.example.geomhelper.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.geomhelper.Resources.Colors;
import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;

public class FragmentThemes extends Fragment {

    public FragmentThemes() {
    }

    Colors colors = new Colors();
    Button[] button;
    LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_themes, container, false);

        linearLayout = view.findViewById(R.id.linearLayoutFragmentThemes);
        button = new Button[Person.currentCourse.getNumberOfThemes()];

        for (int i = 0; i < button.length; i++) {
            button[i] = new Button(getContext());
            button[i].setHeight(250);
            button[i].setBackgroundColor(colors.colors[i]);
            button[i].setText(Person.currentCourse.getTheme(i));
            button[i].setTextSize(16);
            button[i].setId(i);
            linearLayout.addView(button[i]);
            button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.back = 2;
                    Person.currentTheme = v.getId();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    FragmentCourseText fragmentCourseText = new FragmentCourseText();
                    fragmentTransaction.replace(R.id.fragment, fragmentCourseText);
                    fragmentTransaction.commit();
                }
            });
        }
        return view;
    }

}
