package com.example.geomhelper;

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
    private static int[] basicsExperienceOfEachTheme = {15, 20, 10, 10, 10, 30};
    public static Test basics = new Test("Начальные геометрические сведения",
            6,
            basicsThemes,
            basicsExperience,
            basicsExperienceOfEachTheme,
            R.drawable.background_test_1);
    // end basics

    // Second
    private static String[] secondThemes = {"Первый признак равенства треугольников",
            "Медианы, биссектрисы и высоты треугольника",
            "Второй и третий признаки равенства треугольников",
            "Задачи на построение"};
    private static int secondExperience = 150;
    private static int[] secondExperienceOfEachTheme = {50, 30, 50, 20};
    public static Test second = new Test("Треугольники",
            4,
            secondThemes,
            secondExperience,
            secondExperienceOfEachTheme,
            R.drawable.background_test_2);
    // end second

    // Third
    private static String[] thirdThemes = {"Признаки параллельности двух прямых",
            "Аксиома параллельных прямых",
            "Практические задачи"};
    private static int thirdExperience = 100;
    private static int[] thirdExperienceOfEachTheme = {40, 35, 25};
    public static Test third = new Test("Параллельные прямые",
            3,
            thirdThemes,
            thirdExperience,
            thirdExperienceOfEachTheme,
            R.drawable.background_test_3);
    // end third

    // Fourth
    private static String[] fourthThemes = {"Сумма углов треугольника",
            "Соотношения между сторонами и углами",
            "Прямоугольные треугольники",
            "Построение треугольника по трем элементам"};
    private static int fourthExperience = 160;
    private static int[] fourthExperienceOfEachTheme = {30, 30, 50, 50};
    public static Test fourth = new Test("Соотношения между сторонами и углами ▲",
            4,
            fourthThemes,
            fourthExperience,
            fourthExperienceOfEachTheme,
            R.drawable.background_test_4);
    // end fourth

}
