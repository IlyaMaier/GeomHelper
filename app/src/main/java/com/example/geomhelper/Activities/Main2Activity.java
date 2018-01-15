package com.example.geomhelper.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.geomhelper.Courses;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;

public class Main2Activity extends AppCompatActivity {

    Button[] button;
    int i;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        if (Person.courses.size() == Courses.currentCourses.size()) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            Toast.makeText(getApplicationContext(), "Для вас нет доступных курсов", Toast.LENGTH_LONG).show();
            finish();
        } else {
            linearLayout = findViewById(R.id.linearLayoutMain2Activity);
            initializeButtons();
        }
    }

    void initializeButtons() {
        button = new Button[Courses.currentCourses.size()];
        for (i = 0; i < Courses.currentCourses.size(); i++) {
            if (Person.courses.contains(Courses.currentCourses.get(i))) continue;
            button[i] = new Button(getApplicationContext());
            button[i].setBackgroundResource(R.drawable.backround_button_courses);
            button[i].setText(Courses.currentCourses.get(i).getCourseName());
            button[i].setTextSize(16);
            button[i].setId(i);
            linearLayout.addView(button[i]);
            button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Person.courses.add(Courses.currentCourses.get(v.getId()));
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }
}
