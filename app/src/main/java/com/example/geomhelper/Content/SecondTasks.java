package com.example.geomhelper.Content;

import com.example.geomhelper.R;

import java.util.ArrayList;

public class SecondTasks {

    public ArrayList<SecondTask> getTasks(int test, int theme) {
        ArrayList<SecondTask> secondTasks = new ArrayList<>();
        switch (test) {
            case 0:
                switch (theme) {
                    case 0:
                        secondTasks.add(straightAndCut);
                        break;
                }
                break;
        }
        return secondTasks;
    }

    //прямая и отрезок
    private  SecondTask straightAndCut = new SecondTask(
            "Сколько отрезков с концами в отмеченных точках изображено на рисунке?",
            R.drawable.straight_and_cut_2,
            "7");
}