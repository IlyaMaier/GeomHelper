package com.example.geomhelper.Fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    List<Test> currentTests;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tests, container, false);

        currentTests = new Tests().getCurrentTests();

        verticalManager = new LinearLayoutManager(getContext());
        horizontalManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        rvTestsAdapter = new RVAdpater(currentTests);

        fragmentManager = getFragmentManager();

        recyclerView = rootView.findViewById(R.id.rv_tests);
        recyclerView.setLayoutManager(verticalManager);
        recyclerView.setAdapter(rvTestsAdapter);
        recyclerView.scrollToPosition(currentTests.size() - 1);

        bottomNavigationView = Objects.requireNonNull(getActivity()).findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
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

            holder.imageView.setBackgroundResource(test.getBackground());
            holder.name.setText(test.getTestName());

        }

        @Override
        public int getItemCount() {
            return tests.size();
        }

        class TestViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            ImageView imageView;
            CardView cardView;
            Test test;

            public void setTest(Test test) {
                this.test = test;
            }

            TestViewHolder(final View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.text_rv_tests);
                imageView = itemView.findViewById(R.id.card_tests_image);
                cardView = itemView.findViewById(R.id.card_tests);

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Person.currentTest = currentTests.indexOf(test);
                        MainActivity.back = 3;
                        Person.backTests = 3;
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentTestThemes fragmentTestThemes = new FragmentTestThemes();
                        fragmentTransaction.replace(R.id.frame_tests, fragmentTestThemes);
                        fragmentTransaction.commit();
                        recyclerView.setVisibility(View.INVISIBLE);
                        recyclerView.setClickable(false);
                    }
                });
            }

        }

    }

}
