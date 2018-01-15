package com.example.geomhelper.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.geomhelper.R;

public class FragmentTests extends Fragment {

    public FragmentTests() {
    }

    Button button1, button2, button3, button4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tests, container, false);
        button1 = rootView.findViewById(R.id.button_test_1);
        button2 = rootView.findViewById(R.id.button_test_2);
        button3 = rootView.findViewById(R.id.button_test_3);
        button4 = rootView.findViewById(R.id.button_test_4);
        return rootView;
    }

}
