package com.example.geomhelper.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.geomhelper.R;
import com.example.geomhelper.Resources.RVLeaderboardAdapter;
import com.example.geomhelper.Resources.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentLeaderboard extends Fragment {

    public FragmentLeaderboard() {
    }

    RecyclerView recyclerView;
    List<User> data;
    RelativeLayout relativeLayout;
    RVLeaderboardAdapter rvLeaderboardAdapter;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        relativeLayout = rootView.findViewById(R.id.frame_leaderboard);

        List<User> users = getDataFromFirebase();

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

        return rootView;
    }

    List<User> getDataFromFirebase() {
        data = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            data.add(new User("", ""));
        }
        final DatabaseReference f = FirebaseDatabase.getInstance().getReference();
        f.child("leaderboard").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                do
                    for (int i = 1; i < 11; i++) {
                        try {
                            data.get(i - 1).setName(dataSnapshot.child(i + "").child("name").getValue().toString());
                            data.get(i - 1).setExperience(dataSnapshot.child(i + "").child("experience").getValue().toString());
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    } while (q());
                rvLeaderboardAdapter.setData(data);
                rvLeaderboardAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return data;
    }

    boolean q() {
        for (int i = 0; i < 10; i++)
            if (data.get(i).getName() == null || data.get(i).getExperience() == null || data.get(i).getExperience().isEmpty() || data.get(i).getName().isEmpty())
                return true;
        return false;
    }
}
