package com.example.geomhelper.Content;

import com.example.geomhelper.R;

import java.util.ArrayList;
import java.util.List;

public class Tests {

    public List<Test> getCurrentTests() {
        List<Test> tests = new ArrayList<>();
        tests.add(0, basics);
        tests.add(0, second);
        tests.add(0, third);
        tests.add(0, fourth);
        return tests;
    }

    //basics
    private String[] basicsThemes = {"Прямая и отрезок",
            "Луч и угол",
            "Сравнение отрезков и углов",
            "Измерение отрезков",
            "Измерение углов",
            "Перпендикулярные прямые"};
    private int basicsExperience = 95;
    public Test basics = new Test("Начальные геометрические сведения",
            6,
            basicsThemes,
            basicsExperience,
            R.drawable.background_test_1);
    // end basics

    // Second
    private String[] secondThemes = {"Первый признак равенства треугольников",
            "Медианы, биссектрисы и высоты треугольника",
            "Второй и третий признаки равенства треугольников",
            "Задачи на построение"};
    private int secondExperience = 150;
    public Test second = new Test("Треугольники",
            4,
            secondThemes,
            secondExperience,
            R.drawable.background_test_2);
    // end second

    // Third
    private String[] thirdThemes = {"Признаки параллельности двух прямых",
            "Аксиома параллельных прямых",
            "Практические задачи"};
    private int thirdExperience = 100;
    public Test third = new Test("Параллельные прямые",
            3,
            thirdThemes,
            thirdExperience,
            R.drawable.background_test_3);
    // end third

    // Fourth
    private String[] fourthThemes = {"Сумма углов треугольника",
            "Соотношения между сторонами и углами",
            "Прямоугольные треугольники",
            "Построение треугольника по трем элементам"};
    private int fourthExperience = 160;
    public Test fourth = new Test("Соотношения между сторонами и углами ▲",
            4,
            fourthThemes,
            fourthExperience,
            R.drawable.background_test_4);
    // end fourth

}
