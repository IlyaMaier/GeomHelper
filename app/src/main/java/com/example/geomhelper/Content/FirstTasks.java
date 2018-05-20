package com.example.geomhelper.Content;

import com.example.geomhelper.R;

import java.util.ArrayList;

public class FirstTasks {

    public ArrayList<FirstTask> getTasks(int test, int theme) {
        ArrayList<FirstTask> firstTasks = new ArrayList<>();
        switch (test) {
            case 0:
                switch (theme) {
                    case 0:
                        firstTasks.add(straightAndCut);
                        firstTasks.add(straightAndCut1);
                        break;
                }
                break;
        }
        return firstTasks;
    }

    //Прямая и отрезок
    private FirstTask straightAndCut = new FirstTask(
            "Пересекаются ли отрезки AB и CD?",
            R.drawable.straight_and_cut,
            "Да",
            "Нет",
            "Невозможно определить",
            1,
            5);

    private FirstTask straightAndCut1 = new FirstTask(
            "Пересекаются ли прямые АВ и CD?",
            R.drawable.straight_and_cut,
            "Да",
            "Нет",
            "Невозможно определить",
            2,
            5);

}