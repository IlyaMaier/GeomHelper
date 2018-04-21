package com.example.geomhelper.Content;

import com.example.geomhelper.R;

import java.util.ArrayList;
import java.util.List;

public class Tests {

    public static List<Test> currentTests = new ArrayList<>();

    //basics
    private static String[] basicsThemes = {"Прямая и отрезок",
            "Луч и угол",
            "Сравнение отрезков и углов",
            "Измерение отрезков",
            "Измерение углов",
            "Перпендикулярные прямые"};
    private static int basicsExperience = 95;
    public static Test basics = new Test("Начальные геометрические сведения",
            6,
            basicsThemes,
            basicsExperience,
            R.drawable.background_test_1);
    // end basics

    // Second
    private static String[] secondThemes = {"Первый признак равенства треугольников",
            "Медианы, биссектрисы и высоты треугольника",
            "Второй и третий признаки равенства треугольников",
            "Задачи на построение"};
    private static int secondExperience = 150;
    public static Test second = new Test("Треугольники",
            4,
            secondThemes,
            secondExperience,
            R.drawable.background_test_2);
    // end second

    // Third
    private static String[] thirdThemes = {"Признаки параллельности двух прямых",
            "Аксиома параллельных прямых",
            "Практические задачи"};
    private static int thirdExperience = 100;
    public static Test third = new Test("Параллельные прямые",
            3,
            thirdThemes,
            thirdExperience,
            R.drawable.background_test_3);
    // end third

    // Fourth
    private static String[] fourthThemes = {"Сумма углов треугольника",
            "Соотношения между сторонами и углами",
            "Прямоугольные треугольники",
            "Построение треугольника по трем элементам"};
    private static int fourthExperience = 160;
    public static Test fourth = new Test("Соотношения между сторонами и углами ▲",
            4,
            fourthThemes,
            fourthExperience,
            R.drawable.background_test_4);
    // end fourth

}
