package com.femiproject.schoolsystem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Course {
    @JsonProperty
    private String courseCode;
    @JsonProperty
    private String courseTitle;
    @JsonProperty
    private int units;
    @JsonProperty
    private Lecturer lecturer;
    @JsonIgnore
    private List<Student> enrolledStudents;
    @JsonProperty
    private Map<String, Double> studentScores;

    @JsonCreator
    public Course(
            @JsonProperty("courseCode") String courseCode,
            @JsonProperty("courseTitle") String courseTitle,
            @JsonProperty("units") int units,
            @JsonProperty("lecturer") Lecturer lecturer,
            @JsonProperty("studentScores") Map<String, Double> studentScores) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.units = units;
        this.lecturer = lecturer;
        this.enrolledStudents = new ArrayList<>();
        this.studentScores = studentScores != null ? studentScores : new HashMap<>();
    }

    public Course(String courseCode, String courseTitle, int units, Lecturer lecturer) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.units = units;
        this.lecturer = lecturer;
        this.enrolledStudents = new ArrayList<>();
        this.studentScores = new HashMap<>();
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public int getUnits() {
        return units;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public List<Student> getEnrolledStudents() {
        return new ArrayList<>(enrolledStudents);
    }

    public int getEnrollmentCount() {
        return enrolledStudents.size();
    }

    public void enrollStudent(Student student) {
        if (student != null && !enrolledStudents.contains(student)) {
            enrolledStudents.add(student);
            System.out.println("Student " + student.getName() + " enrolled in " + courseTitle);
        }
    }

    public void removeStudent(Student student) {
        if (student != null && enrolledStudents.remove(student)) {
            if (student.getStudentId() != null) {
                studentScores.remove(student.getStudentId());
            }
            System.out.println("Student " + student.getName() + " removed from " + courseTitle);
        }
    }

    public boolean isStudentEnrolled(Student student) {
        return student != null && enrolledStudents.contains(student);
    }

    public void setStudentScore(Student student, double score) {
        if (student != null && student.getStudentId() != null && isStudentEnrolled(student)) {
            if (score >= 0 && score <= 100) {
                studentScores.put(student.getStudentId(), score);
                System.out.println("Score " + score + " set for " + student.getName() + " in " + courseTitle);
            } else {
                System.out.println("Invalid score. Score must be between 0 and 100.");
            }
        } else {
            System.out.println("Cannot set score: Student not enrolled in this course.");
        }
    }

    public Double getStudentScore(Student student) {
        if (student != null && student.getStudentId() != null) {
            return studentScores.get(student.getStudentId());
        }
        return null;
    }

    public Map<String, Double> getAllScores() {
        return new HashMap<>(studentScores);
    }

    public double getAverageScore() {
        if (studentScores.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        for (Double score : studentScores.values()) {
            total += score;
        }
        return total / studentScores.size();
    }

    public double getHighestScore() {
        if (studentScores.isEmpty()) {
            return 0.0;
        }
        return studentScores.values().stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
    }

    public double getLowestScore() {
        if (studentScores.isEmpty()) {
            return 0.0;
        }
        return studentScores.values().stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
    }

    public int getStudentsWithScores() {
        return studentScores.size();
    }

    public List<Student> getStudentsWithoutScores() {
        List<Student> studentsWithoutScores = new ArrayList<>();
        for (Student student : enrolledStudents) {
            if (student.getStudentId() != null && !studentScores.containsKey(student.getStudentId())) {
                studentsWithoutScores.add(student);
            }
        }
        return studentsWithoutScores;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Course course = (Course) obj;
        return courseCode != null && courseCode.equals(course.courseCode);
    }

    @Override
    public int hashCode() {
        return courseCode != null ? courseCode.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Course [courseCode=" + courseCode + ", courseTitle=" + courseTitle + ", units=" + units + ", lecturer="
                + lecturer + ", enrolledStudents=" + enrolledStudents.size() + ", studentsWithScores="
                + studentScores.size() + "]";
    }
}