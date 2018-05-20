package com.example.geomhelper.Content;

public class Levels {

    private String level1 = "Неволшебник-младший";
    private String level2 = "Неволшебник-старший";
    private String level3 = "Волшебник-начинающий";
    private String level4 = "Волшебник-продвинутый";
    private String level5 = "Маг";
    private String level6 = "Выпускник Геогрварства";
    private String level7 = "Профессор магии";
    private String level8 = "Магистр 1 ордена";
    private String level9 = "Магистр 2 ордена";
    private String level10 = "Магистр 3 ордена";
    private String level11 = "Гуру магии";

    private int l1 = 50;
    private int l2 = 100;
    private int l3 = 200;
    private int l4 = 400;
    private int l5 = 800;
    private int l6 = 1400;
    private int l7 = 2000;
    private int l8 = 3000;
    private int l9 = 4000;
    private int l10 = 5000;
    private int l11 = 6000;

    public String getLevel(int e) {
        if (e < l1)
            return level1;
        else if (e < l2)
            return level2;
        else if (e < l3)
            return level3;
        else if (e < l4)
            return level4;
        else if (e < l5)
            return level5;
        else if (e < l6)
            return level6;
        else if (e < l7)
            return level7;
        else if (e < l8)
            return level8;
        else if (e < l9)
            return level9;
        else if (e < l10)
            return level10;
        else
            return level11;
    }

    public int getLevelExperience(String level) {
        if (level.equals(level1))
            return l1;
        if (level.equals(level2))
            return l2;
        if (level.equals(level3))
            return l3;
        if (level.equals(level4))
            return l4;
        if (level.equals(level5))
            return l5;
        if (level.equals(level6))
            return l6;
        if (level.equals(level7))
            return l7;
        if (level.equals(level8))
            return l8;
        if (level.equals(level9))
            return l9;
        if (level.equals(level10))
            return l10;
        if (level.equals(level11))
            return l11;
        return 0;
    }
}
