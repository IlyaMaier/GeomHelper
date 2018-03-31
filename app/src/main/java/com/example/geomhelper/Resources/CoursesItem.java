package com.example.geomhelper.Resources;

import com.example.geomhelper.Course;
import com.example.geomhelper.Courses;
import com.example.geomhelper.Person;

import java.util.ArrayList;

public class CoursesItem {
    private Course course;

    public CoursesItem(Course course){
        this.course = course;
    }

    public Course getCourse(){
        return course;
    }

    public static ArrayList<CoursesItem> getFakeItems(){
        ArrayList<CoursesItem> itemList = new ArrayList<>();

        for (int i = 0; i < Person.courses.size(); i++) {
            itemList.add(new CoursesItem(Courses.currentCourses.get(i)));
        }

        return itemList;
    }

}