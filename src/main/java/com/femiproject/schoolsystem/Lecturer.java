package com.femiproject.schoolsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Lecturer {
    @JsonProperty
    private String lecturerId;
    @JsonProperty
    private String name;
    @JsonIgnore
    private List<Course> coursesTaught;

    @JsonCreator
    public Lecturer(
            @JsonProperty("lecturerId") String lecturerId,
            @JsonProperty("name") String name) {
        this.lecturerId = lecturerId != null ? lecturerId : UUID.randomUUID().toString();
        this.name = name;
        this.coursesTaught = new ArrayList<>();
    }

    public Lecturer(String name) {
        this.lecturerId = UUID.randomUUID().toString();
        this.name = name;
        this.coursesTaught = new ArrayList<>();
    }

    public String getLecturerId() {
        return lecturerId;
    }

    public String getName() {
        return name;
    }

    public List<Course> getCoursesTaught() {
        return new ArrayList<>(coursesTaught);
    }

    public int getCourseCount() {
        return coursesTaught.size();
    }

    public void addCourse(Course course) {
        if (course != null && !coursesTaught.contains(course)) {
            coursesTaught.add(course);
            System.out.println("Course " + course.getCourseTitle() + " assigned to " + name);
        }
    }

    public void removeCourse(Course course) {
        if (course != null && coursesTaught.remove(course)) {
            System.out.println("Course " + course.getCourseTitle() + " removed from " + name);
        }
    }

    public boolean isTeachingCourse(Course course) {
        return course != null && coursesTaught.contains(course);
    }

    public List<Course> getCoursesByTitle(String courseTitle) {
        List<Course> matchingCourses = new ArrayList<>();
        for (Course course : coursesTaught) {
            if (course.getCourseTitle().equalsIgnoreCase(courseTitle)) {
                matchingCourses.add(course);
            }
        }
        return matchingCourses;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Lecturer lecturer = (Lecturer) obj;
        return lecturerId != null && lecturerId.equals(lecturer.lecturerId);
    }

    @Override
    public int hashCode() {
        return lecturerId != null ? lecturerId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Lecturer [lecturerId=" + lecturerId + ", name=" + name + ", coursesTaught=" + coursesTaught.size()
                + "]";
    }
}