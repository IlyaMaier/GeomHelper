package com.example.geomhelper.Fragments;

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

import com.example.geomhelper.R;
import com.example.geomhelper.Resources.RVLeaderboardAdapter;
import com.example.geomhelper.User;
import com.example.geomhelper.UserService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FragmentLeaderboard extends Fragment {

    public FragmentLeaderboard() {
    }

    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    RVLeaderboardAdapter rvLeaderboardAdapter;
    ProgressBar progressBar;
    BottomNavigationView bottomNavigationView;
    List<User> users;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        relativeLayout = rootView.findViewById(R.id.frame_leaderboard);

        users = new ArrayList<>();

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(User.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        UserService userService = retrofit.create(UserService.class);
        userService.getLeaders().enqueue(new Callback<String>() {
                    String result;

                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                            if (Objects.equals(response.body(), "0"))
                                Toast.makeText(getContext(), "Не дуалось получить данные",
                                        Toast.LENGTH_SHORT).show();
                            else {
                                result = response.body();
                                result = Objects.requireNonNull(result).replace("[", "");
                                result = result.replace("]", "");
                                String[] split = result.split(",");
                                Gson gson = new Gson();
                                StringBuilder q = new StringBuilder();
                                int a = 0;
                                for (String aSplit : split) {
                                    if (aSplit.contains("}")) q.append(aSplit);
                                    else q.append(aSplit).append(",");
                                    a++;
                                    if (aSplit.contains("}")) {
                                        users.add(gson.fromJson(q.toString(), User.class));
                                        a = 0;
                                        q = new StringBuilder();
                                    }
                                }

                                rvLeaderboardAdapter.setData(users);
                                rvLeaderboardAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.INVISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), "Не удалось получить данные",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        return rootView;
    }

}
