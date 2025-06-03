package com.femiproject.studentmanagement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentManager manager = new StudentManager();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();

            switch (readInt("Enter your choice: ")) {
                case 1 ->
                    addStudent();
                case 2 ->
                    findStudentById();
                case 3 ->
                    deleteStudent();
                case 4 ->
                    getAllStudents();
                case 5 ->
                    findStudentByName();
                case 6 ->
                    updateStudentName();
                case 7 ->
                    updateStudentGrade();
                case 8 ->
                    findStudentsByPartialName();
                case 9 ->
                    getAverageGrade();
                case 10 ->
                    getTopPerformingStudents();
                case 11 ->
                    getStudentsBySubject();
                case 12 ->
                    getAverageGradePerSubject();
                case 0 -> {
                    System.out.println("Exiting...");
                    running = false;
                }
                default -> {
                    System.out.println("Invalid choice. Try again.");
                }
            }
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("=== Student Management System ===");
        System.out.println("1. Add Student");
        System.out.println("2. Find Student by ID");
        System.out.println("3. Delete Student");
        System.out.println("4. Get All Students");
        System.out.println("5. Find Student by Name");
        System.out.println("6. Update Student Name");
        System.out.println("7. Update Student Grade");
        System.out.println("8. Find Students by Partial Name");
        System.out.println("9. Get Average Grade of a Student");
        System.out.println("10. Get Top Performing Students");
        System.out.println("11. Get Students by Subject");
        System.out.println("12. Get Average Grade Per Subject");
        System.out.println("0. Exit");
    }

    private static int readInt(String prompt) {
        System.out.println(prompt);
        return Integer.parseInt(scanner.nextLine());
    }

    private static double readDouble(String prompt) {
        System.out.print(prompt);
        return Double.parseDouble(scanner.nextLine());
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static void addStudent() {
        String name = readLine("Enter student name: ");
        Map<String, Double> subjects = new HashMap<>();

        while (true) {
            String subject = readLine("Enter subject name (or 'done' to finish): ");
            if (subject.equalsIgnoreCase("done")) {
                break;
            }

            double grade;
            try {
                grade = readDouble("Enter grade for " + subject + ": ");
            } catch (NumberFormatException e) {
                System.out.println("Invalid grade. Please enter a number.");
                continue;
            }
            subjects.put(subject, grade);
        }

        Student student = new Student(name, subjects);
        manager.addStudents(student);
    }

    private static void findStudentById() {
        int studentId = readInt("Enter Student Id: ");
        Student student = manager.findStudentById(studentId);

        if (student != null) {
            System.out.println("Student found:");
            System.out.println(student);
        } else {
            System.out.println("Student not found with ID: " + studentId);
        }
    }

    private static void deleteStudent() {
        int studentId = readInt("Enter Student Id: ");
        Student student = manager.findStudentById(studentId);

        if (student != null) {
            manager.deleteStudent(studentId);
        } else {
            System.out.println("Student not found with ID: " + studentId);
        }
    }

    private static void getAllStudents() {
        if (manager.getAllStudents().isEmpty()) {
            System.out.println("No students found.");
        } else {
            System.out.println("List of Students:");
            for (Student student : manager.getAllStudents()) {
                System.out.println(student);
            }
        }
    }

    private static void findStudentByName() {
        String name = readLine("Enter student name: ");
        Student student = manager.findStudentByName(name);
        if (student == null) {
            System.out.println("Student not found");
        } else {
            System.out.println("Student Found");
            System.out.println(student);
        }
    }

    private static void updateStudentName() {
        int id = readInt("Enter student ID: ");
        String name = readLine("Enter new name: ");
        Student student = manager.findStudentById(id);

        if (student != null) {
            manager.updateStudentName(id, name);
        } else {
            System.out.println("Student ID not found");
        }
    }

    private static void updateStudentGrade() {
        int id = readInt("Enter student ID: ");
        Student student = manager.findStudentById(id);

        if (student == null) {
            System.out.println("Student not found with ID: " + id);
            return;
        }

        String subject = readLine("Enter subject name: ");
        double grade = readDouble("Enter new grade: ");

        boolean success = manager.updateStudentGrade(id, subject, grade);
        if (!success) {
            System.out.println("Failed to update grade.");
        }
    }

    private static void findStudentsByPartialName() {
        String partialName = readLine("Enter part of the student name: ");
        List<Student> matches = manager.findStudentsByPartialName(partialName);

        if (matches.isEmpty()) {
            System.out.println("No students found with name containing: " + partialName);
        } else {
            System.out.println("Matching students:");
            for (Student student : matches) {
                System.out.println(student);
            }
        }
    }

    private static void getAverageGrade() {
        int id = readInt("Enter student ID: ");
        Student student = manager.findStudentById(id);

        if (student != null) {
            double average = manager.getAverageGrade(id);
            System.out.printf("Average grade for %s (ID %d): %.2f%n", student.getName(), id, average);
        } else {
            System.out.println("Student ID not found.");
        }
    }

    private static void getTopPerformingStudents() {
        int count = readInt("How many top students do you want to view? ");

        List<Student> topStudents = manager.getTopPerformingStudents(count);

        if (topStudents.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("Top Performing Students:");
        for (Student student : topStudents) {
            double avg = manager.getAverageGrade(student.getId());
            System.out.printf("ID: %d | Name: %s | Average Grade: %.2f%n",
                    student.getId(), student.getName(), avg);
        }
    }

    private static void getStudentsBySubject() {
        String subject = readLine("Enter subject: ");
        List<Student> students = manager.getStudentsBySubject(subject);

        if (students.isEmpty()) {
            System.out.println("No students found for subject: " + subject);
            return;
        }

        System.out.println("Students enrolled in " + subject + ":");
        for (Student student : students) {
            Double grade = student.getSubjectGrades().get(subject);
            System.out.printf("ID: %d | Name: %s | Grade in %s: %.2f%n",
                    student.getId(), student.getName(), subject, grade);
        }
    }

    private static void getAverageGradePerSubject() {
        Map<String, Double> averages = manager.getAverageGradePerSubject();

        if (averages.isEmpty()) {
            System.out.println("No subjects found.");
            return;
        }

        System.out.println("Average Grades per Subject:");
        for (Map.Entry<String, Double> entry : averages.entrySet()) {
            System.out.printf("Subject: %s | Average Grade: %.2f%n", entry.getKey(), entry.getValue());
        }
    }

}
