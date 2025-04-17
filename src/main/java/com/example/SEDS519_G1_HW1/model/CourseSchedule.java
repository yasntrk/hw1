package com.example.SEDS519_G1_HW1.model;

public class CourseSchedule {
    private String day;
    private String time;
    private String course;

    public CourseSchedule(String day, String time, String course) {
        this.day = day;
        this.time = time;
        this.course = course;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public String getCourse() {
        return course;
    }
}
