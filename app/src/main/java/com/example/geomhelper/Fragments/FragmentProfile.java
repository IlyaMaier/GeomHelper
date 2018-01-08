package com.example.geomhelper.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.geomhelper.Person;
import com.example.geomhelper.R;

public class FragmentProfile extends Fragment {

    TextView textName, textLevelName, textExperience;
    static TextView personName;

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
        return rootView;
    }

}
