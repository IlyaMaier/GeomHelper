package com.example.geomhelper.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;

public class FragmentResult extends Fragment {

    public FragmentResult() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result, container, false);

        rootView.findViewById(R.id.button_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.back = 3;
                Person.backTests = 3;
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                FragmentTestThemes fragmentTestThemes = new FragmentTestThemes();
                fragmentTransaction.replace(R.id.frame_tests, fragmentTestThemes);
                fragmentTransaction.commit();
            }
        });
        return rootView;
    }
}
