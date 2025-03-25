package model;

public class ScheduleEntry {
    private String day;
    private String time;
    private String course;

    public ScheduleEntry(String day, String time, String course) {
        this.day = day;
        this.time = time;
        this.course = course;
    }

    public String getDay() { return day; }
    public String getTime() { return time; }
    public String getCourse() { return course; }
}
