package com.example.geomhelper.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.geomhelper.R;

import java.util.Objects;

public class FragmentSecondTask extends Fragment {

    TextView textViewName2, textViewTask2;
    ImageView imageView2;
    EditText editText2Task;
    Button buttonEnter2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_second_task, container, false);

        textViewName2 = rootView.findViewById(R.id.textViewName2);
        textViewTask2 = rootView.findViewById(R.id.textViewTask2);

        imageView2 = rootView.findViewById(R.id.imageView2);

        editText2Task = rootView.findViewById(R.id.editText2Task);

        buttonEnter2 = rootView.findViewById(R.id.buttonEnter2);

        buttonEnter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentThirdTask fragmentThirdTask = new FragmentThirdTask();
                FragmentManager fragmentManager = getFragmentManager();
                Objects.requireNonNull(fragmentManager).beginTransaction().replace(
                        R.id.frame_tests, fragmentThirdTask).addToBackStack(null).commit();
            }
        });
        return rootView;
    }

}