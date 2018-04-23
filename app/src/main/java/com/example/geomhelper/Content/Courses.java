package com.example.geomhelper.Content;

import com.example.geomhelper.R;

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
            basicCourseTextUrl,
            R.drawable.backround_button_courses);
    // end basics

    // Second
    private static String[] secondThemes = {"Первый признак равенства треугольников",
            "Медианы, биссектрисы и высоты треугольника",
            "Второй и третий признаки равенства треугольников",
            "Задачи на построение"};
    private static String[] secondCourseTextUrl = {
            "file:///android_asset/courses/second/text1.html",
            "file:///android_asset/courses/second/text2.html",
            "file:///android_asset/courses/second/text3.html",
            "file:///android_asset/courses/second/text4.html"
    };
    public static Course second = new Course("Треугольники",
            4,
            secondThemes,
            secondCourseTextUrl,
            R.drawable.backround_button_courses);
    // end second

    // Third
    private static String[] thirdThemes = {"Признаки параллельности двух прямых",
            "Аксиома параллельных прямых",
            "Практические задачи"};
    private static String[] thirdCourseTextUrl = {
            "file:///android_asset/courses/third/text1.html",
            "file:///android_asset/courses/third/text2.html",
            "file:///android_asset/courses/third/text3.html"
    };
    public static Course third = new Course("Параллельные прямые",
            3,
            thirdThemes,
            thirdCourseTextUrl,
            R.drawable.backround_button_courses);
    // end third

    // Fourth
    private static String[] fourthThemes = {"Сумма углов треугольника",
            "Соотношения между сторонами и углами",
            "Прямоугольные треугольники",
            "Построение треугольника по трем элементам"};
    private static String[] fourthCourseTextUrl = {
            "file:///android_asset/courses/fourth/text1.html",
            "file:///android_asset/courses/fourth/text2.html",
            "file:///android_asset/courses/fourth/text3.html",
            "file:///android_asset/courses/fourth/text4.html"
    };
    public static Course fourth = new Course("Соотношения между сторонами и углами ▲",
            4,
            fourthThemes,
            fourthCourseTextUrl,
            R.drawable.backround_button_courses);
    // end fourth

}
