package com.example.geomhelper.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.geomhelper.Activities.AddCourseActivity;
import com.example.geomhelper.Content.Course;
import com.example.geomhelper.Content.Courses;
import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.kinvey.android.Client;
import com.kinvey.android.model.User;
import com.kinvey.java.core.KinveyClientCallback;

import java.util.ArrayList;
import java.util.Objects;

public class FragmentCourses extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager verticalManager;
    RVAdapter adapterCourses;
    FloatingActionButton floatingActionButton;
    View rootView;
    FragmentManager fragmentManager;
    int scrollDist = 0;
    boolean isVisible = true;
    float MINIMUM = 25;
    BottomNavigationView bottomNavigationView;
    Client mKinveyClient;

    public FragmentCourses() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_courses, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerCourses);

        fragmentManager = getFragmentManager();

        verticalManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(verticalManager);

        adapterCourses = new RVAdapter(getContext(), Person.courses);
        recyclerView.setAdapter(adapterCourses);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.simple_grow);

        floatingActionButton = rootView.findViewById(R.id.floatingActionButton);
        floatingActionButton.startAnimation(animation);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AddCourseActivity.class);
                startActivityForResult(i, 10);
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (isVisible && scrollDist > MINIMUM) {
                    hide();
                    scrollDist = 0;
                    isVisible = false;
                } else if (!isVisible && scrollDist < -MINIMUM) {
                    show();
                    scrollDist = 0;
                    isVisible = true;
                }

                if ((isVisible && dy > 0) || (!isVisible && dy < 0)) {
                    scrollDist += dy;
                }

            }

            void show() {
                floatingActionButton.animate().translationY(0)
                        .setInterpolator(new DecelerateInterpolator(2)).start();
            }

            void hide() {
                floatingActionButton.animate().translationY(
                        floatingActionButton.getHeight() + 16).
                        setInterpolator(new AccelerateInterpolator(2)).start();
            }

        });

        bottomNavigationView = Objects.requireNonNull(getActivity()).findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemReselectedListener(
                new BottomNavigationView.OnNavigationItemReselectedListener() {
                    @Override
                    public void onNavigationItemReselected(@NonNull MenuItem item) {
                        recyclerView.smoothScrollToPosition(0);
                    }
                }
        );

        mKinveyClient = new Client.Builder("kid_B1OS_p1hM",
                "602d7fccc790477ca6505a1daa3aa894",
                Objects.requireNonNull(getActivity().getApplicationContext())).
                setBaseUrl("https://baas.kinvey.com").build();

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            adapterCourses.setItems(Person.courses);
            adapterCourses.notifyDataSetChanged();
        }

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
            return new RecyclerViewCoursesHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RVAdapter.RecyclerViewCoursesHolder holder, int position) {
            holder.bind(Person.courses.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        void setItems(ArrayList<Course> items) {
            this.items = items;
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
                        Person.currentCourse = course;
                        MainActivity.back = 1;
                        Person.backCourses = 1;
                        floatingActionButton.hide();
                        recyclerView.setVisibility(View.INVISIBLE);
                        recyclerView.setClickable(false);
                        recyclerView = null;
                        floatingActionButton = null;
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentThemes fragmentThemes = new FragmentThemes();
                        fragmentTransaction.replace(R.id.fragment, fragmentThemes);
                        fragmentTransaction.commit();
                    }
                });

                cardView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        AlertDialog.Builder ad;
                        ad = new AlertDialog.Builder(context);
                        ad.setTitle("Удаление курса");
                        ad.setMessage("Вы точно хотите удалить данный курс?");
                        ad.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                Person.courses.remove(course);
                                adapterCourses.setItems(Person.courses);
                                adapterCourses.notifyDataSetChanged();
                                Person.c = Person.c.replace(String.valueOf(
                                        Courses.currentCourses.indexOf(course)), "");
                                Person.map.put("courses", Person.c);
                                User user = mKinveyClient.getActiveUser();
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
                        });
                        ad.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.cancel();
                            }
                        });
                        ad.setCancelable(true);
                        ad.show();
                        return false;
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