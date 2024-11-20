package com.example.notesharingapp.Model;

public class Course {
    private String title;
    private String type; // File type
    private String url;  // File download URL

    public Course() {
        // Default constructor for Firebase
    }

    public Course(String title, String type, String url) {
        this.title = title;
        this.type = type;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }
}
