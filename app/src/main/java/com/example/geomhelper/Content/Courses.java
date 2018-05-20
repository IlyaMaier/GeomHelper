package com.example.geomhelper.Content;

import com.example.geomhelper.R;

import java.util.ArrayList;
import java.util.List;

public class Courses {

    public List<Course> getCurrentCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(basics);
        courses.add(second);
        courses.add(third);
        courses.add(fourth);
        return courses;
    }

    // Basics
    private String[] basicsThemes = {"Прямая и отрезок",
            "Луч и угол",
            "Сравнение отрезков и углов",
            "Измерение отрезков",
            "Измерение углов",
            "Перпендикулярные прямые"};
    private String[] basicCourseTextUrl = {
            "file:///android_asset/courses/first/text1.html",
            "file:///android_asset/courses/first/text2.html",
            "file:///android_asset/courses/first/text3.html",
            "file:///android_asset/courses/first/text4.html",
            "file:///android_asset/courses/first/text5.html",
            "file:///android_asset/courses/first/text6.html"
    };
    private String[] basicCourseTextUrlNight = {
            "file:///android_asset/courses-night/first/text1.html",
            "file:///android_asset/courses-night/first/text2.html",
            "file:///android_asset/courses-night/first/text3.html",
            "file:///android_asset/courses-night/first/text4.html",
            "file:///android_asset/courses-night/first/text5.html",
            "file:///android_asset/courses-night/first/text6.html"
    };
    private Course basics = new Course("Начальные геометрические сведения",
            6,
            basicsThemes,
            basicCourseTextUrl,
            basicCourseTextUrlNight,
            R.drawable.backround_button_courses);
    // end basics

    // Second
    private String[] secondThemes = {"Первый признак равенства треугольников",
            "Медианы, биссектрисы и высоты треугольника",
            "Второй и третий признаки равенства треугольников",
            "Задачи на построение"};
    private String[] secondCourseTextUrl = {
            "file:///android_asset/courses/second/text1.html",
            "file:///android_asset/courses/second/text2.html",
            "file:///android_asset/courses/second/text3.html",
            "file:///android_asset/courses/second/text4.html"
    };
    private String[] secondCourseTextUrlNight = {
            "file:///android_asset/courses-night/second/text1.html",
            "file:///android_asset/courses-night/second/text2.html",
            "file:///android_asset/courses-night/second/text3.html",
            "file:///android_asset/courses-night/second/text4.html"
    };
    private Course second = new Course("Треугольники",
            4,
            secondThemes,
            secondCourseTextUrl,
            secondCourseTextUrlNight,
            R.drawable.backround_button_courses);
    // end second

    // Third
    private String[] thirdThemes = {"Признаки параллельности двух прямых",
            "Аксиома параллельных прямых",
            "Практические задачи"};
    private String[] thirdCourseTextUrl = {
            "file:///android_asset/courses/third/text1.html",
            "file:///android_asset/courses/third/text2.html",
            "file:///android_asset/courses/third/text3.html"
    };
    private String[] thirdCourseTextUrlNight = {
            "file:///android_asset/courses-night/third/text1.html",
            "file:///android_asset/courses-night/third/text2.html",
            "file:///android_asset/courses-night/third/text3.html"
    };
    private Course third = new Course("Параллельные прямые",
            3,
            thirdThemes,
            thirdCourseTextUrl,
            thirdCourseTextUrlNight,
            R.drawable.backround_button_courses);
    // end third

    // Fourth
    private String[] fourthThemes = {"Сумма углов треугольника",
            "Соотношения между сторонами и углами",
            "Прямоугольные треугольники",
            "Построение треугольника по трем элементам"};
    private String[] fourthCourseTextUrl = {
            "file:///android_asset/courses/fourth/text1.html",
            "file:///android_asset/courses/fourth/text2.html",
            "file:///android_asset/courses/fourth/text3.html",
            "file:///android_asset/courses/fourth/text4.html"
    };
    private String[] fourthCourseTextUrlNight = {
            "file:///android_asset/courses-night/fourth/text1.html",
            "file:///android_asset/courses-night/fourth/text2.html",
            "file:///android_asset/courses-night/fourth/text3.html",
            "file:///android_asset/courses-night/fourth/text4.html"
    };
    private Course fourth = new Course("Соотношения между сторонами и углами ▲",
            4,
            fourthThemes,
            fourthCourseTextUrl,
            fourthCourseTextUrlNight,
            R.drawable.backround_button_courses);
    // end fourth

}
