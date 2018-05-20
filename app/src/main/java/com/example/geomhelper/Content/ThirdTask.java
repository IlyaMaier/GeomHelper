package com.example.geomhelper.Content;

public class ThirdTask {
    private String textViewT31, textViewT32, textViewT33;
    private int imgThirdTask;
    private String[] answer;

    ThirdTask(String textViewT31, String textViewT32,
                     String textViewT33, int imThirdTask, String[] answer) {
        this.textViewT31 = textViewT31;
        this.textViewT32 = textViewT32;
        this.textViewT33 = textViewT33;
        this.imgThirdTask = imThirdTask;
        this.answer = answer;
    }


    public String getTextViewT31() {
        return textViewT31;
    }

    public void setTextViewT31(String textViewT31) {
        this.textViewT31 = textViewT31;
    }

    public String getTextViewT32() {
        return textViewT32;
    }

    public void setTextViewT32(String textViewT32) {
        this.textViewT32 = textViewT32;
    }

    public String getTextViewT33() {
        return textViewT33;
    }

    public void setTextViewT33(String textViewT33) {
        this.textViewT33 = textViewT33;
    }

    public int getImgThirdTask() {
        return imgThirdTask;
    }

    public void setImgThirdTask(int imgThirdTask) {
        this.imgThirdTask = imgThirdTask;
    }

    public String[] getAnswer() {
        return answer;
    }

    public void setAnswer(String[] answer) {
        this.answer = answer;
    }
}