package com.example.onlinequizapp;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Lesson {
    public String time;
    public String date;
    public String fullname;
    public String instructor;
    public String lessonId;
    public String userId;
    public String lessonUIslot;

    public Lesson() {

    }

    public Lesson(String time, String date, String fullname, String instructor, String lessonId, String userId, String lessonUIslot) {
        this.time = time;
        this.date = date;
        this.fullname = fullname;
        this.instructor = instructor;
        this.lessonId= lessonId;
        this.userId= userId;
        this.lessonUIslot= lessonUIslot;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getFullname() {
        return fullname;
    }

    public String getInstructor() {
        return instructor;
    }
    public String getLessonId() {
        return lessonId;
    }
    public String getUserId() {
        return userId;
    }
    public String getLessonUIslot() {
        return lessonUIslot;
    }
}

