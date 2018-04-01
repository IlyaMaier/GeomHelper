package com.example.geomhelper.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.Resources.Resources;

public class FragmentTestThemes extends Fragment {

    Resources resources = new Resources();
    Button[] button;
    LinearLayout linearLayout;

    public FragmentTestThemes() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_test_themes, container, false);

        linearLayout = rootView.findViewById(R.id.linearLayoutFragmentTestThemes);
        button = new Button[Person.currentTest.getNumberOfThemes()];

        for (int i = 0; i < button.length; i++) {
            button[i] = new Button(getContext());
            button[i].setHeight(250);
            button[i].setBackgroundColor(resources.colors[i]);
            button[i].setText(Person.currentTest.getTheme(i));
            button[i].setTextSize(16);
            button[i].setId(i);
            linearLayout.addView(button[i]);
            button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    MainActivity.back = 4;
//                    Person.backTests = 4;
//                    Person.currentTestTheme = v.getId();
//                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                    FragmentCourseText fragmentCourseText = new FragmentCourseText();
//                    fragmentTransaction.replace(R.id.fragment, fragmentCourseText);
//                    fragmentTransaction.commit();
                }
            });
        }

        return rootView;

    }



}
