package com.example.geomhelper.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geomhelper.Content.Course;
import com.example.geomhelper.Content.Courses;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.kinvey.android.Client;
import com.kinvey.java.core.KinveyClientCallback;

import java.util.ArrayList;

public class AddCourseActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RVAdapter rvAdapter;
    LinearLayoutManager linearLayoutManager;

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
            ArrayList<Course> courses = new ArrayList<>();
            Course course;
            for (int i = 0; i < Courses.currentCourses.size(); i++) {
                course = Courses.currentCourses.get(i);
                if (!Person.courses.contains(course)) courses.add(course);
            }

            recyclerView = findViewById(R.id.recycler_add_course);
            linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            rvAdapter = new RVAdapter(getApplicationContext(), courses);
            recyclerView.setAdapter(rvAdapter);
        }
    }

    void initializeButtons() {
//        button = new Button[Courses.currentCourses.size()];
//        for (i = 0; i < Courses.currentCourses.size(); i++) {
//            if (Person.courses.contains(Courses.currentCourses.get(i))) continue;
//            button[i] = new Button(getApplicationContext());
//            button[i].setBackgroundResource(R.drawable.backround_button_add_course);
//            button[i].setText(Courses.currentCourses.get(i).getCourseName());
//            button[i].setTextSize(16);
//            button[i].setTextColor(getResources().getColor(R.color.textColor));
//            button[i].setId(i);
//            linearLayout.addView(button[i]);
//            button[i].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Person.courses.add(0, Courses.currentCourses.get(v.getId()));
//                    sendDataToFirebase(v.getId() + "");
//                    Intent intent = new Intent();
//                    setResult(RESULT_OK, intent);
//                    finish();
//                }
//            });
//        }
    }

    void sendDataToFirebase(String name) {
        Person.c += name;
        Person.map.put("courses", Person.c);
        Client mKinveyClient = new Client.Builder("kid_B1OS_p1hM",
                "602d7fccc790477ca6505a1daa3aa894",
                this.getApplicationContext()).setBaseUrl("https://baas.kinvey.com").build();
        com.kinvey.android.model.User user = mKinveyClient.getActiveUser();
        user.putAll(Person.map);
        user.update(new KinveyClientCallback() {
            @Override
            public void onSuccess(Object o) {

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RecyclerViewCoursesHolder> {

        ArrayList<Course> items;
        Context context;

        RVAdapter(Context context, ArrayList<Course> items) {
            this.items = items;
            this.context = context;
        }

        @NonNull
        @Override
        public RVAdapter.RecyclerViewCoursesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_courses_card, parent, false);
            return new RVAdapter.RecyclerViewCoursesHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RVAdapter.RecyclerViewCoursesHolder holder, int position) {
            holder.bind(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class RecyclerViewCoursesHolder extends RecyclerView.ViewHolder {

            private CardView cardView;
            private TextView title;
            private ImageView image;
            private Course course;

            RecyclerViewCoursesHolder(final View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
                image = itemView.findViewById(R.id.image_content_courses);
                cardView = itemView.findViewById(R.id.card_content_courses);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Person.courses.add(0, course);
                        sendDataToFirebase(v.getId() + "");
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

            }

            void bind(Course course) {
                this.course = course;
                image.setImageBitmap(BitmapFactory.decodeResource(itemView.getResources(), course.getBackground()));
                title.setText(course.getCourseName());
            }

        }

    }

}
