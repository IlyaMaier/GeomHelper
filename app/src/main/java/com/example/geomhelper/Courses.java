package com.example.geomhelper;

import java.util.ArrayList;
import java.util.List;

public class Courses {

    public static List<Course> currentCourses = new ArrayList<>();

    // Basics
    private static String[] basicsThemes = {"Прямая и отрезок",
            "Луч и угол",
            "Сравнение отрезков и углов",
            "Измерение отрезков",
            "Измерение углов",
            "Перпендикулярные прямые"};
    private static String[] basicThemesText = {"Ну кароче открой учебник и прочитай", "2", "3", "4", "5", "6"};
    private static int basicsExperience = 95;
    private static   int[] basicsExperienceOfEachTheme = {15, 20, 10, 10, 10, 30};

    public static Course basics = new Course("Начальные геометрические сведения",
            6,
            basicsThemes,
            basicThemesText,
            basicsExperience,
            basicsExperienceOfEachTheme);
    // end basics

    // Second
    private static String[] secondThemes = {"Первый признак равенства треугольников",
            "Медианы, биссектрисы и высоты треугольника",
            "Второй и третий признаки равенства треугольников",
            "Задачи на построение"};
    private static String[] secondThemesText = {"1", "2", "3", "4"};
    private static int secondExperience = 150;
    private static   int[] secondExperienceOfEachTheme = {50, 30, 50, 20};
    public static Course second = new Course("Треугольники",
            4,
            secondThemes,
            secondThemesText,
            secondExperience,
            secondExperienceOfEachTheme);
    // end second

}
