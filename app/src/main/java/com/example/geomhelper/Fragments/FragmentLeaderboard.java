package com.example.geomhelper.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    boolean w = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        List<User> users = getDataFromFirebase();

        recyclerView = rootView.findViewById(R.id.rv_leaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new RVLeaderboardAdapter(getContext(), users));

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
                w = true;
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
