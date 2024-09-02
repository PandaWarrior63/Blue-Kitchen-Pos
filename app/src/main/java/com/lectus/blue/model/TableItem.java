package com.lectus.blue.model;

public class TableItem {
    private String title;
    private String description;
    private String background;

    public TableItem(String title, String description) {
        this.title = title;
        this.description = description;
        this.background = "#DCDCDC";
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}