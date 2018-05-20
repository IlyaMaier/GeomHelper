package com.example.geomhelper.Content;
import com.example.geomhelper.R;

import java.util.ArrayList;

public class ThirdTasks {

    public ArrayList<ThirdTask> getTasks(int test, int theme) {
        ArrayList<ThirdTask> thirdTask = new ArrayList<>();
        switch (test) {
            case 0:
                switch (theme) {
                    case 0:
                        thirdTask.add(test1);
                        thirdTask.add(test1);
                        break;
                }
                break;
        }
        return thirdTask;
    }

    //test
    private String[] testTask = {"Сколько отрезков изображено на рисунке?",
            "Сколько прямых изображено на рисунке?",
            "Сколько точек изображено на рисунке?"};
    private String[] testAnswer = {"2","0","4"};
    private ThirdTask test1 = new ThirdTask(testTask[0],
            testTask[1],testTask[2], R.drawable.straight_and_cut, testAnswer);
}