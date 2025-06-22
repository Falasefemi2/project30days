package com.femiproject.schoolsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Student {
    @JsonProperty
    private String studentId;

    @JsonProperty
    private String name;

    @JsonProperty
    private String level;

    @JsonProperty
    private List<Course> courses;

    @JsonCreator
    public Student(
            @JsonProperty String studentId,
            @JsonProperty String name,
            @JsonProperty String level,
            @JsonProperty List<Course> courses) {
        this.studentId = studentId;
        this.name = name;
        this.level = level;
        this.courses = courses != null ? courses : new ArrayList<>();
    }

    public Student(String name, String level) {
        this.studentId = UUID.randomUUID().toString();
        this.name = name;
        this.level = level;
        this.courses = new ArrayList<>();
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getLevel() {
        return level;
    }

    public List<Course> getCourses() {
        return courses;
    }

    @Override
    public String toString() {
        return "Student [studentId=" + studentId + ", name=" + name + ", level=" + level + ", courses=" + courses.size()
                + "]";
    }

    public void registerCourse(Course course) {
        if (course != null && !courses.contains(course)) {
            courses.add(course);
            System.out.println("Course " + course.getCourseTitle() + " registered successfully for " + name);
        }
    }

    public void unregisterCourse(Course course) {
        if (course != null && courses.remove(course)) {
            System.out.println("Course " + course.getCourseTitle() + " unregistered for " + name);
        }
    }

    public List<Course> getAllRegisteredCourses() {
        return new ArrayList<>(courses);
    }

    public int getTotalUnits() {
        return courses.stream().mapToInt(Course::getUnits).sum();
    }

    public double calculateCGPA() {
        if (courses.isEmpty()) {
            return 0.0;
        }

        double totalGradePoints = 0.0;
        int totalUnits = 0;

        for (Course course : courses) {
            Double score = course.getStudentScore(this);
            if (score != null) {
                int units = course.getUnits();
                double gpa = convertScoreToGPA(score);
                totalGradePoints += (gpa * units);
                totalUnits += units;
            }
        }

        return totalUnits > 0 ? totalGradePoints / totalUnits : 0.0;
    }

    public double calculateCGPAWithScores() {
        if (courses.isEmpty()) {
            return 0.0;
        }

        double totalWeightedScore = 0.0;
        int totalUnits = 0;

        for (Course course : courses) {
            Double score = course.getStudentScore(this);
            if (score != null) {
                int units = course.getUnits();
                totalWeightedScore += (score * units);
                totalUnits += units;
            }
        }

        return totalUnits > 0 ? totalWeightedScore / totalUnits : 0.0;
    }

    private double convertScoreToGPA(double score) {
        if (score >= 90)
            return 4.0;
        else if (score >= 80)
            return 3.5;
        else if (score >= 70)
            return 3.0;
        else if (score >= 60)
            return 2.5;
        else if (score >= 50)
            return 2.0;
        else if (score >= 40)
            return 1.5;
        else if (score >= 30)
            return 1.0;
        else
            return 0.0;
    }

    public List<Course> getCoursesWithScores() {
        List<Course> coursesWithScores = new ArrayList<>();
        for (Course course : courses) {
            if (course.getStudentScore(this) != null) {
                coursesWithScores.add(course);
            }
        }
        return coursesWithScores;
    }

    public List<Course> getCoursesWithoutScores() {
        List<Course> coursesWithoutScores = new ArrayList<>();
        for (Course course : courses) {
            if (course.getStudentScore(this) == null) {
                coursesWithoutScores.add(course);
            }
        }
        return coursesWithoutScores;
    }

    public int getCoursesWithScoresCount() {
        return getCoursesWithScores().size();
    }

    public double getAverageScore() {
        List<Course> coursesWithScores = getCoursesWithScores();
        if (coursesWithScores.isEmpty()) {
            return 0.0;
        }

        double totalScore = 0.0;
        for (Course course : coursesWithScores) {
            Double score = course.getStudentScore(this);
            if (score != null) {
                totalScore += score;
            }
        }
        return totalScore / coursesWithScores.size();
    }

    public String getAcademicStatus() {
        double cgpa = calculateCGPA();
        if (cgpa >= 3.5)
            return "Distinction";
        else if (cgpa >= 3.0)
            return "Good Standing";
        else if (cgpa >= 2.0)
            return "Satisfactory";
        else if (cgpa >= 1.0)
            return "Warning";
        else
            return "Probation";
    }
}
