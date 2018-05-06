package com.example.geomhelper;

import android.content.SharedPreferences;

import com.example.geomhelper.Content.Course;
import com.example.geomhelper.Content.Test;

import java.util.ArrayList;

public class Person {

    public static final String APP_PREFERENCES = "mySettings";
    public static final String APP_PREFERENCES_COURSES_SIZE = "size";
    public static final String APP_PREFERENCES_NAME = "name";
    public static final String APP_PREFERENCES_UID = "uid";
    public static final String APP_PREFERENCES_LEVEL = "level";
    public static final String APP_PREFERENCES_EXPERIENCE = "experience";
    public static final String APP_PREFERENCES_LEVEL_EXPERIENCE = "levelExperience";
    public static final String APP_PREFERENCES_LEADERBOARDPLACE = "leaderboardPlace";
    public static final String APP_PREFERENCES_COURSES = "course";
    public static final String APP_PREFERENCES_WELCOME = "welcome";

    public static String name;
    public static String uId;
    public static String id;
    public static String currentLevel = "Ученик";
    public static int experience = 0;
    public static int currentLevelExperience = 100;
    public static long leaderBoardPlace = 0;
    public static ArrayList<Course> courses = new ArrayList<>();
    public static String c = "";
    public static Course currentCourse;
    public static int currentTheme = 0;
    public static Test currentTest;
    public static int currentTestTheme = 0;
    public static short backTests = 0;
    public static short backCourses = 0;
    public static SharedPreferences pref;

}
