package com.example.geomhelper.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;

import java.util.Objects;

public class FragmentThirdTask extends Fragment {

    TextView textViewName3, textViewTask3;
    ImageView imageView3;
    EditText editText2Task;
    Button buttonEnd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_third_task, container, false);

        textViewName3 = rootView.findViewById(R.id.textViewName3);
        textViewTask3 = rootView.findViewById(R.id.textViewTask3);

        imageView3 = rootView.findViewById(R.id.imageView3);

        editText2Task = rootView.findViewById(R.id.editText2Task);

        buttonEnd = rootView.findViewById(R.id.buttonEnd);
        buttonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.back = 3;
                Person.backTests = 3;
                FragmentManager fragmentManager = getFragmentManager();
                Objects.requireNonNull(fragmentManager).beginTransaction().
                        replace(R.id.frame_tests, new FragmentTestThemes()).commit();
            }
        });

        return rootView;
    }
}