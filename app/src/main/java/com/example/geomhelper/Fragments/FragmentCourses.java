package com.example.geomhelper.Fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import com.example.geomhelper.Activities.AddCourseActivity;
import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.Resources.RVCoursesAdapter;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentCourses extends Fragment {

    static RecyclerView recyclerView;
    LinearLayoutManager verticalManager, horizontalManager;
    static RVCoursesAdapter adapterCourses;
    static FloatingActionButton floatingActionButton;
    View rootView;
    static FragmentManager fragmentManager;
    int scrollDist = 0;
    boolean isVisible = true;
    float MINIMUM = 25;

    public FragmentCourses() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_courses, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerCourses);

        fragmentManager = getFragmentManager();

        verticalManager = new LinearLayoutManager(getContext());
        horizontalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(verticalManager);

        adapterCourses = new RVCoursesAdapter(getContext(), Person.courses);
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

            public void show() {
                floatingActionButton.animate().translationY(0)
                        .setInterpolator(new DecelerateInterpolator(2)).start();
            }

            public void hide() {
                floatingActionButton.animate().translationY(
                        floatingActionButton.getHeight() + 16).
                        setInterpolator(new AccelerateInterpolator(2)).start();
            }
        });
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(horizontalManager);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(verticalManager);
        }
    }

    public static void click() {
        MainActivity.back = 1;
        Person.backCourses = 1;
        floatingActionButton.hide();
        recyclerView.setVisibility(View.INVISIBLE);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentThemes fragmentThemes = new FragmentThemes();
        fragmentTransaction.replace(R.id.fragment, fragmentThemes);
        fragmentTransaction.commit();
    }

    public static void top() {
        recyclerView.smoothScrollToPosition(0);
    }

    public static void delete(String name) {
        adapterCourses.setItems(Person.courses);
        adapterCourses.notifyDataSetChanged();
        try {
            FirebaseDatabase.getInstance().getReference().
                    child(Person.uId).child("courses").child(name).setValue("deleted");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}