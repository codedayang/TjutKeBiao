package link.dayang.tjutname.datasource.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//每一个Slot表示一节Course的位置，当周次不同时这个位置对应的Course不同
public class Slot {
    private final List<Course> courseList;
    private final Map<Integer, Integer> map;

    public Slot() {
        courseList = new ArrayList<>();
        map = new HashMap<>();
    }

    public Course getCourseByWeek(int week) {
        if (map.containsKey(week)) {
            return courseList.get(map.get(week));
        }
        return null;
    }

    private int findCourse(Course course) {
        for (int i = 0; i < courseList.size(); i++) {
            if (courseList.get(i).getTitle().equals(course.getTitle())) {
                return i;
            }
        }
        return -1;
    }

    public void addCourse(Course course, int week) {
        int index = findCourse(course);
        if (index != -1) {
            map.put(week, index);
        } else {
            courseList.add(course);
            map.put(week, courseList.size() - 1);
        }

    }

    public void addCourse(Course course, int fromWeek, int toWeek) {
        for (int i = fromWeek; i <= toWeek; i++) {
            addCourse(course, i);
        }
    }

    public void addCourse(Course course, List<Integer> weeks) {
        for (Integer i : weeks) {
            addCourse(course, i);
        }
    }

    public boolean isEmpty() {
        return courseList.isEmpty();
    }

    @Override
    public String toString() {
        return "Slot{" +
                "courseList=" + courseList +
                ", map=" + map +
                '}';
    }
}
