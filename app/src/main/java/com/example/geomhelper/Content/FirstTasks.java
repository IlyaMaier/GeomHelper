package com.example.geomhelper.Content;

import com.example.geomhelper.R;

import java.util.ArrayList;

public class FirstTasks {

    public static ArrayList<FirstTask> tasks = new ArrayList<>();

    //Прямая и отрезок
    public static FirstTask straightAndCut = new FirstTask(
            "Пересекаются ли отрезки AB и CD?",
            R.drawable.straight_and_cut,
            "Да",
            "Нет",
            "Невозможно определить",
            1,
            5);

    public static FirstTask straightAndCut1 = new FirstTask(
            "Пересекаются ли прямые АВ и CD?",
            R.drawable.straight_and_cut,
            "Да",
            "Нет",
            "Невозможно определить",
            0,
            5);

}