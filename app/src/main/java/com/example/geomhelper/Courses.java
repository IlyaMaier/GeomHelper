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
    private static int basicsExperience = 95;
    private static int[] basicsExperienceOfEachTheme = {15, 20, 10, 10, 10, 30};
    private static String[] basicCourseTextUrl = {
            "file:///android_asset/courses/first/text1.html",
            "file:///android_asset/courses/first/text2.html",
            "file:///android_asset/courses/first/text3.html",
            "file:///android_asset/courses/first/text4.html",
            "file:///android_asset/courses/first/text5.html",
            "file:///android_asset/courses/first/text6.html"
    };
    public static Course basics = new Course("Начальные геометрические сведения",
            6,
            basicsThemes,
            basicsExperience,
            basicsExperienceOfEachTheme,
            basicCourseTextUrl,
            R.drawable.backround_button_courses);
    // end basics

    // Second
    private static String[] secondThemes = {"Первый признак равенства треугольников",
            "Медианы, биссектрисы и высоты треугольника",
            "Второй и третий признаки равенства треугольников",
            "Задачи на построение"};
    private static int secondExperience = 150;
    private static int[] secondExperienceOfEachTheme = {50, 30, 50, 20};
    private static String[] secondCourseTextUrl = {
            "file:///android_asset/courses/second/text1.html",
            "file:///android_asset/courses/second/text2.html",
            "file:///android_asset/courses/second/text3.html",
            "file:///android_asset/courses/second/text4.html"
    };
    public static Course second = new Course("Треугольники",
            4,
            secondThemes,
            secondExperience,
            secondExperienceOfEachTheme,
            secondCourseTextUrl,
            R.drawable.backround_button_courses);
    // end second

    // Third
    private static String[] thirdThemes = {"Признаки параллельности двух прямых",
            "Аксиома параллельных прямых",
            "Практические задачи"};
    private static int thirdExperience = 100;
    private static int[] thirdExperienceOfEachTheme = {40, 35, 25};
    private static String[] thirdCourseTextUrl = {
            "file:///android_asset/courses/third/text1.html",
            "file:///android_asset/courses/third/text2.html",
            "file:///android_asset/courses/third/text3.html"
    };
    public static Course third = new Course("Параллельные прямые",
            3,
            thirdThemes,
            thirdExperience,
            thirdExperienceOfEachTheme,
            thirdCourseTextUrl,
            R.drawable.backround_button_courses);
    // end third

    // Fourth
    private static String[] fourthThemes = {"Сумма углов треугольника",
            "Соотношения между сторонами и углами",
            "Прямоугольные треугольники",
            "Построение треугольника по трем элементам"};
    private static int fourthExperience = 160;
    private static int[] fourthExperienceOfEachTheme = {30, 30, 50, 50};
    private static String[] fourthCourseTextUrl = {
            "file:///android_asset/courses/fourth/text1.html",
            "file:///android_asset/courses/fourth/text2.html",
            "file:///android_asset/courses/fourth/text3.html",
            "file:///android_asset/courses/fourth/text4.html"
    };
    public static Course fourth = new Course("Соотношения между сторонами и углами ▲",
            4,
            fourthThemes,
            fourthExperience,
            fourthExperienceOfEachTheme,
            fourthCourseTextUrl,
            R.drawable.backround_button_courses);
    // end fourth

}
