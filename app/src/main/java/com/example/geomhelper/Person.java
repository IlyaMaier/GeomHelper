package com.example.geomhelper;

import java.util.ArrayList;

public class Person {

    // это будет именем файла настроек
    public static final String APP_PREFERENCES = "mySettings";
    public static final String APP_PREFERENCES_COURSES_SIZE = "size";
    public static final String APP_PREFERENCES_NAME = "name";
    public static final String APP_PREFERENCES_LEVEL = "level";
    public static final String APP_PREFERENCES_EXPERIENCE = "experience";
    public static final String APP_PREFERENCES_LEVEL_EXPERIENCE = "levelExperience";
    public static final String APP_PREFERENCES_LEADERBOARDPLACE = "leaderboardPlace";
    public static final String APP_PREFERENCES_COURSES = "course";
    public static final String APP_PREFERENCES_WELCOME = "welcome";


    public static String name = "";
    public static String currentLevel = "Ученик";
    public static int experience = 0;
    public static int currentLevelExperience = 100;
    public static long leaderBoardPlace;
    public static ArrayList<Course> courses = new ArrayList<>();
    public static Course currentCourse = null;
    public static int currentTheme = 0;

}
