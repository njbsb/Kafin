package com.fyp.kafin.model;

public class HomeCard {

    private String title;
    private String content;
    private String color;

    public HomeCard(String title, String content, String color) {
        this.title = title;
        this.content = content;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
