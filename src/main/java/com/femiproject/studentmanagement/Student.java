package com.femiproject.studentmanagement;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Student {

    private static int idCounter = 1;

    private int id;
    private String name;
    private Map<String, Double> subjectGrades;

    public Student() {
    }

    public Student(String name, Map<String, Double> subjectGrades) {
        this.id = idCounter++;
        this.name = name;
        this.subjectGrades = subjectGrades;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public static void setIdCounter(int counter) {
        idCounter = counter;
    }

    public String getName() {
        return name;
    }

    public Map<String, Double> getSubjectGrades() {
        return subjectGrades;
    }

    public void addSubject(String subject, double grade) {
        if (grade < 0 || grade > 100) {
            throw new IllegalArgumentException("Grade must be between 0 and 100.");
        }
        subjectGrades.put(subject, grade);
    }

    public double getAverageGrade() {
        return subjectGrades.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    @Override
    public String toString() {
        return "Student{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", subjectGrades=" + subjectGrades
                + ", average=" + getAverageGrade()
                + '}';
    }
}
