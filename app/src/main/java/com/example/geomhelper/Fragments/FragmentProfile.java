package com.example.geomhelper.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.geomhelper.Person;
import com.example.geomhelper.R;

public class FragmentProfile extends Fragment {

    TextView textLevelName, textExperience, textName;
    static ScrollView scrollView;

    public FragmentProfile() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        textName = rootView.findViewById(R.id.textName);
        textName.setText(Person.name);
        textLevelName = rootView.findViewById(R.id.textLevelName);
        textLevelName.setText(Person.currentLevel);
        textExperience = rootView.findViewById(R.id.textExperince);
        textExperience.setText((Person.experience + "/" + Person.currentLevelExperience));

        scrollView = rootView.findViewById(R.id.scroll_profile);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        textName.setText(Person.name);
    }

    public static void top(){
        scrollView.scrollTo(0,0);
    }
}
