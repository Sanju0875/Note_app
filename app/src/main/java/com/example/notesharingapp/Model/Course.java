package com.example.notesharingapp.Model;

public class Course {
    private int noteImage; // Drawable resource ID
    private String noteTitle;
    private String subjectName;
    private String semester;
    private String faculty;

    public Course(int noteImage, String noteTitle, String subjectName, String semester, String faculty) {
        this.noteImage = noteImage;
        this.noteTitle = noteTitle;
        this.subjectName = subjectName;
        this.semester = semester;
        this.faculty = faculty;
    }

    public int getNoteImage() {
        return noteImage;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getSemester() {
        return semester;
    }

    public String getFaculty() {
        return faculty;
    }
}
