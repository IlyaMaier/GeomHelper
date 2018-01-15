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
    private static int[] basicsExperienceOfEachTheme = {15, 20, 10, 10, 10, 30};

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
    private static int[] secondExperienceOfEachTheme = {50, 30, 50, 20};
    public static Course second = new Course("Треугольники",
            4,
            secondThemes,
            secondThemesText,
            secondExperience,
            secondExperienceOfEachTheme);
    // end second

    // Third
    private static String[] thirdThemes = {"Признаки параллельности двух прямых",
            "Аксиома параллельных прямых",
            "Практические задачи"};
    private static String[] thirdThemesText = {"1", "2", "3"};
    private static int thirdExperience = 100;
    private static int[] thirdExperienceOfEachTheme = {40, 35, 25};
    public static Course third = new Course("Параллельные прямые",
            3,
            thirdThemes,
            thirdThemesText,
            thirdExperience,
            thirdExperienceOfEachTheme);
    // end third

    // Fourth
    private static String[] fourthThemes = {"Сумма углов треугольника",
            "Соотношения между сторонами и углами",
            "Прямоугольные треугольники",
            "Построение треугольника по трем элементам"};
    private static String[] fourthThemesText = {"1", "2", "3", "4"};
    private static int fourthExperience = 160;
    private static int[] fourthExperienceOfEachTheme = {30, 30, 50, 50};
    public static Course fourth = new Course("Соотношения между сторонами и углами ▲",
            4,
            fourthThemes,
            fourthThemesText,
            fourthExperience,
            fourthExperienceOfEachTheme);
    // end fourth

}
