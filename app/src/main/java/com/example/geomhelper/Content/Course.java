package com.example.geomhelper.Content;

public class Course {

    private String courseName;
    private int numberOfThemes;
    private String[] themes;
    private String[] courseTextUrl;
    private String[] courseTextUrlNight;
    private int background;

    Course(String courseName, int numberOfThemes, String[] themes, String[] courseTextUrl,
           String[] courseTextUrlNight, int background) {
        this.courseName = courseName;
        this.numberOfThemes = numberOfThemes;
        this.themes = themes;
        this.courseTextUrl = courseTextUrl;
        this.courseTextUrlNight = courseTextUrlNight;
        this.background = background;
    }

    public String getCourseTextUrl(int a) {
        return courseTextUrl[a];
    }

    public String getCourseTextUrlNight(int a) {
        return courseTextUrlNight[a];
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getNumberOfThemes() {
        return numberOfThemes;
    }

    public void setNumberOfThemes(int numberOfThemes) {
        this.numberOfThemes = numberOfThemes;
    }

    public String getTheme(int i) {
        return themes[i];
    }

    public String[] getTheme() {
        return themes;
    }

    public void setTheme(String theme, int i) {
        this.themes[i] = theme;
    }

    public int getThemesSize() {
        return themes.length;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

}
