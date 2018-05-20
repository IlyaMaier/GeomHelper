package com.example.geomhelper.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.cardview.widget.CardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geomhelper.Content.FirstTask;
import com.example.geomhelper.Content.FirstTasks;
import com.example.geomhelper.Content.Levels;
import com.example.geomhelper.Content.SecondTask;
import com.example.geomhelper.Content.SecondTasks;
import com.example.geomhelper.Content.Test;
import com.example.geomhelper.Content.Tests;
import com.example.geomhelper.Content.ThirdTask;
import com.example.geomhelper.Content.ThirdTasks;
import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.TestJSON;
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

import static com.example.geomhelper.Person.pref;
import static com.example.geomhelper.Person.task;

public class FragmentResult extends Fragment {

    private FirstTask firstTask;
    private SecondTask secondTask;
    private ThirdTask thirdTask;
    static int answer1 = -1;
    static String answer2 = "";
    static String[] answers3 = new String[3];
    private int xp;

    public FragmentResult() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result, container, false);

        initializeTests();

        LinearLayout linearLayout = rootView.findViewById(R.id.ll_result);

        CardView card1 = (CardView) LayoutInflater.from(getContext()).inflate(R.layout.card_result, null);
        CardView card2 = (CardView) LayoutInflater.from(getContext()).inflate(R.layout.card_result, null);
        CardView card3 = (CardView) LayoutInflater.from(getContext()).inflate(R.layout.card_result, null);

        TextView textView = card1.findViewById(R.id.text_number);
        textView.setText("1");
        textView = card2.findViewById(R.id.text_number);
        textView.setText("2");
        textView = card3.findViewById(R.id.text_number);
        textView.setText("3");

        textView = card1.findViewById(R.id.text_answer);
        StringBuilder answer = new StringBuilder("Правильный ответ : " + firstTask.getCorrectAnswer());
        textView.setText(answer.toString());
        textView = card2.findViewById(R.id.text_answer);
        answer = new StringBuilder("Правильный ответ : " + secondTask.getAnswer());
        textView.setText(answer.toString());
        textView = card3.findViewById(R.id.text_answer);
        answer = new StringBuilder("Правильные ответы : ");
        for (int i = 0; i < thirdTask.getAnswer().length; i++)
            answer.append(thirdTask.getAnswer()[i]).append(", ");
        textView.setText(answer.toString());

        if (firstTask.getCorrectAnswer() == answer1) {
            ImageView imageView = card1.findViewById(R.id.image_result);
            imageView.setBackgroundResource(R.drawable.correct);
            xp += 3;
        } else if (answer1 == -1) {
            ImageView imageView = card1.findViewById(R.id.image_result);
            imageView.setBackgroundResource(R.drawable.unchecked);
        } else {
            ImageView imageView = card1.findViewById(R.id.image_result);
            imageView.setBackgroundResource(R.drawable.wrong);
        }
        if (secondTask.getAnswer().equals(answer2)) {
            ImageView imageView = card2.findViewById(R.id.image_result);
            imageView.setBackgroundResource(R.drawable.correct);
            xp += 6;
        } else if (answer2.equals("")) {
            ImageView imageView = card2.findViewById(R.id.image_result);
            imageView.setBackgroundResource(R.drawable.unchecked);
        } else {
            ImageView imageView = card2.findViewById(R.id.image_result);
            imageView.setBackgroundResource(R.drawable.wrong);
        }
        if (thirdTask.getAnswer()[0].equals(answers3[0])
                && thirdTask.getAnswer()[1].equals(answers3[1])
                && thirdTask.getAnswer()[2].equals(answers3[2])) {
            ImageView imageView = card3.findViewById(R.id.image_result);
            imageView.setBackgroundResource(R.drawable.correct);
            xp += 6;
        } else if (answers3[0].equals("")||answers3[1].equals("")||answers3[2].equals("")) {
            ImageView imageView = card3.findViewById(R.id.image_result);
            imageView.setBackgroundResource(R.drawable.unchecked);
        } else {
            ImageView imageView = card3.findViewById(R.id.image_result);
            imageView.setBackgroundResource(R.drawable.wrong);
        }

        answer1 = -1;
        answer2 = "";
        answers3 = new String[3];

        linearLayout.addView(card1);
        linearLayout.addView(card2);
        linearLayout.addView(card3);

        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) card1.getLayoutParams();
        params1.setMargins(0, 30, 0, 0);
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) card2.getLayoutParams();
        params2.setMargins(0, 30, 0, 0);
        LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) card3.getLayoutParams();
        params3.setMargins(0, 30, 0, 15);

        TextView textXP = rootView.findViewById(R.id.experience);
        String t = "+" + xp + "xp";
        textXP.setText(t);

        rootView.findViewById(R.id.button_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Person.experience += xp;
                Person.currentLevel = new Levels().getLevel(Person.experience);
                Person.currentLevelExperience = new Levels().getLevelExperience(Person.currentLevel);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(User.URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                try {
                    UserService userService = retrofit.create(UserService.class);
                    userService.updateUser(Person.id, "experience", String.valueOf(Person.experience))
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                                }

                                @Override
                                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                    Toast.makeText(getContext(),
                                            "Не удалось отправить данные на сервер", Toast.LENGTH_SHORT).show();
                                }
                            });
                } catch (NullPointerException e) {
                    Toast.makeText(getContext(),
                            "Не удалось отправить данные на сервер", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                if (xp > 0) {
                    TestJSON testJSON = new Gson().fromJson(
                            pref.getString("tests", null), TestJSON.class);
                    if (testJSON == null) testJSON = new TestJSON();
                    testJSON.setTest(Person.currentTest, Person.currentTestTheme, task + 1);
                    pref.edit().putString("tests", new Gson().toJson(testJSON, TestJSON.class)).apply();
                    try {
                        UserService userService = retrofit.create(UserService.class);
                        userService.updateUser(Person.id, "tests", new Gson().toJson(testJSON, TestJSON.class))
                                .enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                        Toast.makeText(getContext(),
                                                "Не удалось отправить данные на сервер", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } catch (NullPointerException e) {
                        Toast.makeText(getContext(),
                                "Не удалось отправить данные на сервер", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                MainActivity.back = 3;
                Person.backTests = 3;
                FragmentTransaction fragmentTransaction = Objects.requireNonNull(
                        getFragmentManager()).beginTransaction();
                FragmentTestThemes fragmentTestThemes = new FragmentTestThemes();
                fragmentTransaction.replace(R.id.frame_tests, fragmentTestThemes);
                fragmentTransaction.commit();
            }
        });
        return rootView;
    }

    private void initializeTests() {
        List<Test> tests = new Tests().getCurrentTests();

        ArrayList<FirstTask> firstTasks = new FirstTasks().
                getTasks(tests.size() - 1 - Person.currentTest, Person.currentTestTheme);
        firstTask = firstTasks.get(task);

        ArrayList<SecondTask> secondTasks = new SecondTasks().
                getTasks(tests.size() - 1 - Person.currentTest, Person.currentTestTheme);
        secondTask = secondTasks.get(task);

        ArrayList<ThirdTask> thirdTasks = new ThirdTasks().
                getTasks(tests.size() - 1 - Person.currentTest, Person.currentTestTheme);
        thirdTask = thirdTasks.get(task);
    }

}
