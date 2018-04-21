package com.example.geomhelper.Fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
import android.widget.TextView;

import com.example.geomhelper.Content.Test;
import com.example.geomhelper.Content.Tests;
import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;

import java.util.List;
import java.util.Objects;

public class FragmentTests extends Fragment {

    public FragmentTests() {
    }

    RecyclerView recyclerView;
    FragmentManager fragmentManager;
    RVAdpater rvTestsAdapter;
    LinearLayoutManager verticalManager, horizontalManager;
    BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tests, container, false);

        verticalManager = new LinearLayoutManager(getContext());
        horizontalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        rvTestsAdapter = new RVAdpater(Tests.currentTests);

        fragmentManager = getFragmentManager();

        recyclerView = rootView.findViewById(R.id.rv_tests);
        recyclerView.setLayoutManager(verticalManager);
        recyclerView.setAdapter(rvTestsAdapter);
        recyclerView.scrollToPosition(Tests.currentTests.size() - 1);

        bottomNavigationView = Objects.requireNonNull(getActivity()).
                findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemReselectedListener(
                new BottomNavigationView.OnNavigationItemReselectedListener() {
                    @Override
                    public void onNavigationItemReselected(@NonNull MenuItem item) {
                        recyclerView.smoothScrollToPosition(rvTestsAdapter.getItemCount() - 1);
                    }
                });

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

    class RVAdpater extends RecyclerView.Adapter<RVAdpater.TestViewHolder> {

        private List<Test> tests;

        public void setData(List<Test> t) {
            tests = t;
        }

        RVAdpater(List<Test> tests) {
            this.tests = tests;
        }

        @NonNull
        @Override
        public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_tests_content, parent, false);
            return new TestViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {

            final Test test = tests.get(position);
            holder.test = test;

            holder.cardView.setBackgroundResource(test.getBackground());
            holder.name.setText(test.getTestName());

        }

        @Override
        public int getItemCount() {
            return tests.size();
        }

        class TestViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            CardView cardView;
            Test test;

            public void setTest(Test test) {
                this.test = test;
            }

            TestViewHolder(final View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.text_rv_tests);
                cardView = itemView.findViewById(R.id.card_tests);

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Person.currentTest = test;
                        MainActivity.back = 3;
                        Person.backTests = 3;
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentTestThemes fragmentTestThemes = new FragmentTestThemes();
                        fragmentTransaction.replace(R.id.frameTests, fragmentTestThemes);
                        fragmentTransaction.commit();
                        recyclerView.setVisibility(View.INVISIBLE);
                        recyclerView.setClickable(false);
                    }
                });
            }

        }

    }

}
