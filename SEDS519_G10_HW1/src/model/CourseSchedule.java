package model;

import java.util.*;

public class CourseSchedule {
    private Map<String, Map<String, String>> schedule;

    public CourseSchedule() {
        schedule = new LinkedHashMap<>();
    }

    public void addEntry(ScheduleEntry entry) {
        schedule
            .computeIfAbsent(entry.getDay(), k -> new LinkedHashMap<>())
            .put(entry.getTime(), entry.getCourse());
    }

    public Map<String, Map<String, String>> getSchedule() {
        return schedule;
    }
}
