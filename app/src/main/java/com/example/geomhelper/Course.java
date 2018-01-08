package com.example.geomhelper;

public class Course {
    private String courseName;
    private int numberOfThemes;
    private String[] themes;
    private String[] themesText;
    private int courseExperience;
    private int[] experienceOfEachTheme;


    public Course(String courseName, int numberOfThemes, String[] themes, String[] themesText, int courseExperience, int[] experienceOfEachTheme) {
        this.numberOfThemes = numberOfThemes;
        this.courseName = courseName;
        this.themes = themes;
        this.themesText = themesText;
        this.courseExperience = courseExperience;
        this.experienceOfEachTheme = experienceOfEachTheme;
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

    public void setTheme(String theme, int i) {
        this.themes[i] = theme;
    }

    public int getThemesSize() {
        return themes.length;
    }

    public String getThemesText(int i) {
        return themesText[i];
    }

    public void setThemesTex(String themesText, int i) {
        this.themesText[i] = themesText;
    }

    public int getCourseExperience() {
        return courseExperience;
    }

    public void setCourseExperience(int courseExperience) {
        this.courseExperience = courseExperience;
    }

    public int getExperienceOfEachTheme(int i) {
        return experienceOfEachTheme[i];
    }

    public void setExperienceOfEachTheme(int experienceOfEachTheme, int i) {
        this.experienceOfEachTheme[i] = experienceOfEachTheme;
    }
}
