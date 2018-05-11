package com.example.geomhelper.Content;

public class SecondTask {
    private String textViewName2, textViewTask2, answer;
    private int imageView2;

    SecondTask(String textViewTask2,
                      int imageView2, String answer) {
        this.textViewTask2 = textViewTask2;
        this.imageView2 = imageView2;
        this.answer = answer;
    }

    public String getTextViewName2() {
        return textViewName2;
    }

    public void setTextViewName2(String textViewName2) {
        this.textViewName2 = textViewName2;
    }

    public String getTextViewTask2() {
        return textViewTask2;
    }

    public void setTextViewTask2(String textViewTask2) {
        this.textViewTask2 = textViewTask2;
    }

    public int getImageView2() {
        return imageView2;
    }

    public void setImageView2(int imageView2) {
        this.imageView2 = imageView2;
    }

    public String answer() {
        return answer;
    }

    public void answer(String answer) {
        this.answer = answer;
    }

}