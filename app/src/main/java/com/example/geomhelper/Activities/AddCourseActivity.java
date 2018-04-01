package com.example.geomhelper.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.geomhelper.Courses;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.google.firebase.database.FirebaseDatabase;

public class AddCourseActivity extends AppCompatActivity {

    Button[] button;
    int i;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_course_activity);
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
            button[i].setBackgroundResource(R.drawable.backround_button_add_course);
            button[i].setText(Courses.currentCourses.get(i).getCourseName());
            button[i].setTextSize(16);
            button[i].setTextColor(getResources().getColor(R.color.dark));
            button[i].setId(i);
            linearLayout.addView(button[i]);
            button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Person.courses.add(0,Courses.currentCourses.get(v.getId()));
                    sendDataToFirebase(v.getId() + "");
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }

    void sendDataToFirebase(String name) {
        try {
            FirebaseDatabase.getInstance().getReference().
                    child(Person.uId).child("courses").child(name).setValue("added");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}