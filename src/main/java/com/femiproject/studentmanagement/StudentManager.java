package com.femiproject.studentmanagement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class StudentManager {

    private List<Student> students;

    @JsonIgnore
    private final ObjectMapper objectMapper;
    private static final String studentFile = "student.json";

    public StudentManager() {
        this.students = new ArrayList<>();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        loadStudents();
    }

    public void addStudents(Student student) {
        students.add(student);
        System.out.println("Student added successfully");
        saveStudents();
    }

    public Student findStudentById(int id) {
        return students.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void deleteStudent(int id) {
        Student student = findStudentById(id);
        if (student == null) {
            System.out.println("Student not found");
            return;
        }
        students.remove(student);
        saveStudents();
        System.out.println("Student deleted successfully");
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public Student findStudentByName(String name) {
        return students.stream()
                .filter(s -> s.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public boolean updateStudentName(int id, String newName) {
        Student student = findStudentById(id);
        if (student == null) {
            System.out.println("Student not found");
            return false;
        }
        student.setName(newName);
        saveStudents();
        System.out.println("Name updated successfully");
        return true;
    }

    public boolean updateStudentGrade(int id, String subject, double newGrade) {
        Student student = findStudentById(id);
        if (student == null) {
            System.out.println("Student not found");
            return false;
        }
        student.getSubjectGrades().put(subject, newGrade);
        saveStudents();
        System.out.println("Grade updated successfully");
        return true;
    }

    public List<Student> findStudentsByPartialName(String partialName) {
        return students.stream()
                .filter(s -> s.getName().toLowerCase().contains(partialName.toLowerCase()))
                .toList();
    }

    public double getAverageGrade(int studentId) {
        Student student = findStudentById(studentId);
        if (student == null || student.getSubjectGrades().isEmpty()) {
            return 0;
        }

        return student.getSubjectGrades().values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);
    }

    public List<Student> getTopPerformingStudents(int count) {
        return students.stream()
                .sorted((s1, s2) -> Double.compare(
                getAverageGrade(s2.getId()), getAverageGrade(s1.getId())
        ))
                .limit(count)
                .toList();
    }

    public List<Student> getStudentsBySubject(String subject) {
        return students.stream()
                .filter(s -> s.getSubjectGrades().containsKey(subject))
                .toList();
    }

    public Map<String, Double> getAverageGradePerSubject() {
        Map<String, List<Double>> subjectGrades = new HashMap<>();

        for (Student student : students) {
            for (Map.Entry<String, Double> entry : student.getSubjectGrades().entrySet()) {
                subjectGrades
                        .computeIfAbsent(entry.getKey(), k -> new ArrayList<>())
                        .add(entry.getValue());
            }
        }

        Map<String, Double> result = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : subjectGrades.entrySet()) {
            double avg = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0);
            result.put(entry.getKey(), avg);
        }

        return result;
    }

    private void loadStudents() {
        try {
            File file = new File(studentFile);
            if (file.exists()) {
                students = objectMapper.readValue(file, new TypeReference<List<Student>>() {
                });

                // Set idCounter to max + 1
                int maxId = students.stream()
                        .mapToInt(Student::getId)
                        .max()
                        .orElse(0);
                Student.setIdCounter(maxId + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveStudents() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(studentFile), students);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
