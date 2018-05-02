package com.example.geomhelper.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.geomhelper.Content.Leader;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.Resources.RVLeaderboardAdapter;
import com.kinvey.android.Client;
import com.kinvey.android.store.DataStore;
import com.kinvey.java.KinveyException;
import com.kinvey.java.cache.KinveyCachedClientCallback;
import com.kinvey.java.store.StoreType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.geomhelper.Person.pref;

public class FragmentLeaderboard extends Fragment {

    public FragmentLeaderboard() {
    }

    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    RVLeaderboardAdapter rvLeaderboardAdapter;
    ProgressBar progressBar;
    BottomNavigationView bottomNavigationView;
    Client mKinveyClient;
    DataStore<Leader> dataStore;
    List<Leader> users;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        relativeLayout = rootView.findViewById(R.id.frame_leaderboard);

        users = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            users.add(new Leader("", 0));

        rvLeaderboardAdapter = new RVLeaderboardAdapter(getContext(), users);

        recyclerView = rootView.findViewById(R.id.rv_leaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(rvLeaderboardAdapter);
        recyclerView.setVisibility(View.INVISIBLE);

        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200, 200);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayout.addView(progressBar, params);
        progressBar.setVisibility(View.VISIBLE);

        bottomNavigationView = Objects.requireNonNull(getActivity()).
                findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemReselectedListener(
                new BottomNavigationView.OnNavigationItemReselectedListener() {
                    @Override
                    public void onNavigationItemReselected(@NonNull MenuItem item) {
                        if (item.getItemId() == R.id.navigation_leaderboard) {
                            recyclerView.smoothScrollToPosition(0);
                        }
                    }
                });
        if (pref.getBoolean(Person.APP_PREFERENCES_WELCOME, false)) {
            mKinveyClient = new Client.Builder("kid_B1OS_p1hM",
                    "602d7fccc790477ca6505a1daa3aa894",
                    Objects.requireNonNull(this.getContext())).setBaseUrl("https://baas.kinvey.com").build();

            dataStore = DataStore.collection("leaders",
                    Leader.class, StoreType.CACHE, mKinveyClient);

            Async async = new Async();
            async.onPreExecute();
            async.execute();
        }
        return rootView;
    }

    class Async extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                try {
                    dataStore.find(new KinveyCachedClientCallback<List<Leader>>() {
                        @Override
                        public void onSuccess(List<Leader> leaders) {
                            users = leaders;
                            publishProgress();
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (KinveyException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            rvLeaderboardAdapter.setData(users);
            rvLeaderboardAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
