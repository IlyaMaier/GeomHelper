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

import com.example.geomhelper.Activities.AddCourseActivity;
import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.Resources.CoursesItem;
import com.example.geomhelper.Resources.RVCoursesAdapter;

import java.util.ArrayList;

public class FragmentCourses extends Fragment {

    static RecyclerView recyclerView;
    LinearLayoutManager verticalManager, horizontalManager;
    RVCoursesAdapter adapterCourses;
    static FloatingActionButton floatingActionButton;
    View rootView;
    public static ArrayList<CoursesItem> itemList;
    static FragmentManager fragmentManager;
    int i = 0;

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

        itemList = CoursesItem.getFakeItems();
        adapterCourses = new RVCoursesAdapter(getContext(), itemList);
        recyclerView.setAdapter(adapterCourses);

        floatingActionButton = rootView.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AddCourseActivity.class);
                startActivityForResult(i, 10);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            adapterCourses.setItems(itemList);
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

    public static void top(){
        recyclerView.smoothScrollToPosition(0);
    }

}