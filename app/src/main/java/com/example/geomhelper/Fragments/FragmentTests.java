package com.example.geomhelper.Fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.Resources.RVTestsAdapter;
import com.example.geomhelper.Tests;

public class FragmentTests extends Fragment {

    public FragmentTests() {
    }

    static RecyclerView recyclerView;
    static FragmentManager fragmentManager;
    RVTestsAdapter rvTestsAdapter;
    LinearLayoutManager verticalManager, horizontalManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tests, container, false);

        verticalManager = new LinearLayoutManager(getContext());
        horizontalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        rvTestsAdapter = new RVTestsAdapter(getContext(), Tests.currentTests);

        fragmentManager = getFragmentManager();

        recyclerView = rootView.findViewById(R.id.rv_tests);
        recyclerView.setLayoutManager(verticalManager);
        recyclerView.setAdapter(rvTestsAdapter);
        recyclerView.scrollToPosition(Tests.currentTests.size()-1);

        return rootView;
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

    public static void bottom() {
        recyclerView.smoothScrollToPosition(Tests.currentTests.size()-1);
    }

    public static void click(){
        MainActivity.back = 3;
        Person.backTests = 3;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentTestThemes fragmentTestThemes = new FragmentTestThemes();
        fragmentTransaction.replace(R.id.frameTests, fragmentTestThemes);
        fragmentTransaction.commit();
        recyclerView.setVisibility(View.INVISIBLE);
        recyclerView.setClickable(false);
    }

}
