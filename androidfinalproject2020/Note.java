package com.example.androidfinalproject2020;

public class Note {

    private String title;
    private String description;
    private int number;
    private int calorie;

    public Note() {
    }

    public Note(String title, String description, int number, int calorie) {
        this.title = title;
        this.description = description;
        this.number = number;
        this.calorie = calorie;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }
}
