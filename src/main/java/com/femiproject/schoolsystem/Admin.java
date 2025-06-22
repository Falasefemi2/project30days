package com.femiproject.schoolsystem;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.*;

import java.util.Scanner;

public class Admin {
    private List<Student> students;
    private List<Lecturer> lecturers;
    private List<Course> courses;
    private Scanner scanner;
    private ObjectMapper objectMapper;
    private static final String STUDENTS_FILE = "students.json";
    private static final String LECTURERS_FILE = "lecturers.json";
    private static final String COURSES_FILE = "courses.json";

    public Admin() {
        this.scanner = new Scanner(System.in);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.students = new ArrayList<>();
        this.lecturers = new ArrayList<>();
        this.courses = new ArrayList<>();

        loadData();
    }

    public void start() {
        System.out.println("Welcome to school portal");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nSaving data before exit...");
            saveData();
        }));

        while (true) {
            printMenu();
            int choice = getIntInput("Choose an option: ");

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> removeStudent();
                case 3 -> listStudents();
                case 4 -> addLecturer();
                case 5 -> removeLecturer();
                case 6 -> listLecturers();
                case 7 -> addCourse();
                case 8 -> removeCourse();
                case 9 -> listCourses();
                case 10 -> enrollStudentInCourse();
                case 11 -> setStudentScore();
                case 12 -> generateStudentReport();
                case 13 -> generateCourseReport();
                case 0 -> {
                    saveData();
                    System.out.println("Bye");
                    return;
                }
                default -> System.out.println("Invalid option try again.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n== School Portal ==");
        System.out.println("1. Add Student");
        System.out.println("2. Remove Student");
        System.out.println("3. List Student");
        System.out.println("4. Add Lecturer");
        System.out.println("5. Remove Lecturer");
        System.out.println("6. List Lecturer");
        System.out.println("7. Add Course");
        System.out.println("8. Remove Course");
        System.out.println("9. List Course");
        System.out.println("10. Enroll Student to course");
        System.out.println("11. Grade student score");
        System.out.println("12. Generate student report");
        System.out.println("13. Generate course report");
        System.out.println("0. Exit");
    }

    public void saveData() {
        try {
            objectMapper.writeValue(new File(STUDENTS_FILE), students);
            objectMapper.writeValue(new File(LECTURERS_FILE), lecturers);
            objectMapper.writeValue(new File(COURSES_FILE), courses);
            System.out.println("Data saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    public void loadData() {
        try {
            File studentsFile = new File(STUDENTS_FILE);
            File lecturersFile = new File(LECTURERS_FILE);
            File coursesFile = new File(COURSES_FILE);

            if (lecturersFile.exists()) {
                lecturers = objectMapper.readValue(lecturersFile, new TypeReference<List<Lecturer>>() {
                });
                System.out.println("Lecturers loaded: " + lecturers.size());
            }

            if (coursesFile.exists()) {
                courses = objectMapper.readValue(coursesFile, new TypeReference<List<Course>>() {
                });
                System.out.println("Courses loaded: " + courses.size());

                for (Course course : courses) {
                    if (course.getLecturer() != null) {
                        String lecturerId = course.getLecturer().getLecturerId();
                        Lecturer lecturer = findLecturerById(lecturerId);
                        if (lecturer != null && !lecturer.isTeachingCourse(course)) {
                            lecturer.addCourse(course);
                        }
                    }
                }
            }

            if (studentsFile.exists()) {
                students = objectMapper.readValue(studentsFile, new TypeReference<List<Student>>() {
                });
                System.out.println("Students loaded: " + students.size());

                for (Student student : students) {
                    for (Course course : student.getAllRegisteredCourses()) {
                        String courseCode = course.getCourseCode();
                        Course actualCourse = findCourseByCode(courseCode);
                        if (actualCourse != null && !actualCourse.isStudentEnrolled(student)) {
                            actualCourse.enrollStudent(student);
                        }
                    }
                }
            }

            System.out.println("Data loaded successfully!");
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Student findStudentById(String studentId) {
        return students.stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst()
                .orElse(null);
    }

    public Lecturer findLecturerById(String lecturerId) {
        return lecturers.stream()
                .filter(l -> l.getLecturerId().equals(lecturerId))
                .findFirst()
                .orElse(null);
    }

    public Course findCourseByCode(String courseCode) {
        return courses.stream()
                .filter(c -> c.getCourseCode().equals(courseCode))
                .findFirst()
                .orElse(null);
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public void addStudent() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter student level: ");
        String level = scanner.nextLine();

        var student = new Student(name, level);
        students.add(student);
        System.out.println("Student added successfully! ID: " + student.getStudentId());
        saveData();
    }

    public void removeStudent() {
        System.out.print("Enter student ID to remove: ");
        String studentId = scanner.nextLine();

        var studentToRemove = findStudentById(studentId);
        if (studentToRemove != null) {
            for (Course course : courses) {
                course.removeStudent(studentToRemove);
            }
            students.remove(studentToRemove);
            System.out.println("Student removed successfully!");
            saveData();
        } else {
            System.out.println("Student not found!");
        }
    }

    public void listStudents() {
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        System.out.println("\n=== STUDENT LIST ===");
        for (Student student : students) {
            System.out.printf("ID: %s | Name: %s | Level: %s | Courses: %d | CGPA: %.2f\n",
                    student.getStudentId(), student.getName(), student.getLevel(),
                    student.getAllRegisteredCourses().size(), student.calculateCGPA());
        }
    }

    public void addLecturer() {
        System.out.print("Enter lecturer name: ");
        String name = scanner.nextLine();

        var lecturer = new Lecturer(name);
        lecturers.add(lecturer);
        System.out.println("Lecturer added successfully! ID: " + lecturer.getLecturerId());
        saveData();
    }

    public void removeLecturer() {
        System.out.print("Enter lecturer ID to remove: ");
        String lecturerId = scanner.nextLine();

        Lecturer lecturerToRemove = findLecturerById(lecturerId);
        if (lecturerToRemove != null) {

            for (Course course : courses) {
                if (course.getLecturer() != null && course.getLecturer().getLecturerId().equals(lecturerId)) {

                    Course updatedCourse = new Course(course.getCourseCode(), course.getCourseTitle(),
                            course.getUnits(), null);

                    for (Student student : course.getEnrolledStudents()) {
                        updatedCourse.enrollStudent(student);
                        Double score = course.getStudentScore(student);
                        if (score != null) {
                            updatedCourse.setStudentScore(student, score);
                        }
                    }
                    int courseIndex = courses.indexOf(course);
                    courses.set(courseIndex, updatedCourse);
                }
            }
            lecturers.remove(lecturerToRemove);
            System.out.println("Lecturer removed successfully!");
            saveData();
        } else {
            System.out.println("Lecturer not found!");
        }
    }

    public void listLecturers() {
        if (lecturers.isEmpty()) {
            System.out.println("No lecturers found.");
            return;
        }
        System.out.println("\n=== LECTURER LIST ===");
        for (Lecturer lecturer : lecturers) {
            System.out.printf("ID: %s | Name: %s | Courses: %d\n",
                    lecturer.getLecturerId(), lecturer.getName(), lecturer.getCourseCount());
        }
    }

    public void addCourse() {
        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine();

        if (findCourseByCode(courseCode) != null) {
            System.out.println("Course with code " + courseCode + " already exists!");
            return;
        }

        System.out.print("Enter course title: ");
        String courseTitle = scanner.nextLine();
        System.out.print("Enter course units: ");
        int units = Integer.parseInt(scanner.nextLine());

        Lecturer lecturer = null;
        if (!lecturers.isEmpty()) {
            System.out.println("Available lecturers:");
            listLecturers();
            System.out.print("Enter lecturer ID (or press Enter to skip): ");
            String lecturerId = scanner.nextLine();
            if (!lecturerId.isEmpty()) {
                lecturer = findLecturerById(lecturerId);
                if (lecturer == null) {
                    System.out.println("Lecturer not found. Course will be created without lecturer.");
                }
            }
        }

        var course = new Course(courseCode, courseTitle, units, lecturer);
        courses.add(course);

        if (lecturer != null) {
            lecturer.addCourse(course);
        }

        System.out.println("Course added successfully!");
        saveData();
    }

    public void removeCourse() {
        System.out.print("Enter course code to remove: ");
        String courseCode = scanner.nextLine();

        Course courseToRemove = findCourseByCode(courseCode);
        if (courseToRemove != null) {
            for (Student student : students) {
                student.unregisterCourse(courseToRemove);
            }
            if (courseToRemove.getLecturer() != null) {
                courseToRemove.getLecturer().removeCourse(courseToRemove);
            }
            courses.remove(courseToRemove);
            System.out.println("Course removed successfully!");
            saveData();
        } else {
            System.out.println("Course not found!");
        }
    }

    public void listCourses() {
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        System.out.println("\n=== COURSE LIST ===");
        for (Course course : courses) {
            String lecturerName = course.getLecturer() != null ? course.getLecturer().getName() : "Unassigned";
            System.out.printf("Code: %s | Title: %s | Units: %d | Lecturer: %s | Students: %d | Avg Score: %.2f\n",
                    course.getCourseCode(), course.getCourseTitle(), course.getUnits(),
                    lecturerName, course.getEnrollmentCount(), course.getAverageScore());
        }
    }

    public void enrollStudentInCourse() {
        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine();

        Student student = findStudentById(studentId);
        Course course = findCourseByCode(courseCode);

        if (student != null && course != null) {
            if (course.isStudentEnrolled(student)) {
                System.out.println("Student is already enrolled in this course!");
                return;
            }
            course.enrollStudent(student);
            student.registerCourse(course);
            System.out.println("Student enrolled successfully!");
            saveData();
        } else {
            System.out.println("Student or course not found!");
        }
    }

    public void setStudentScore() {
        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine();
        System.out.print("Enter score (0-100): ");
        double score = Double.parseDouble(scanner.nextLine());

        Student student = findStudentById(studentId);
        Course course = findCourseByCode(courseCode);

        if (student != null && course != null) {
            course.setStudentScore(student, score);
            saveData();
        } else {
            System.out.println("Student or course not found!");
        }
    }

    public void generateStudentReport() {
        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();

        Student student = findStudentById(studentId);
        if (student != null) {
            System.out.println("\n=== STUDENT REPORT ===");
            System.out.printf("Name: %s\n", student.getName());
            System.out.printf("ID: %s\n", student.getStudentId());
            System.out.printf("Level: %s\n", student.getLevel());
            System.out.printf("Total Units: %d\n", student.getTotalUnits());
            System.out.printf("CGPA: %.2f\n", student.calculateCGPA());
            System.out.printf("Academic Status: %s\n", student.getAcademicStatus());

            System.out.println("\nEnrolled Courses:");
            for (Course course : student.getAllRegisteredCourses()) {
                Double score = course.getStudentScore(student);
                String scoreStr = score != null ? String.format("%.1f", score) : "Not graded";
                System.out.printf("  %s - %s (%d units): %s\n",
                        course.getCourseCode(), course.getCourseTitle(), course.getUnits(), scoreStr);
            }
        } else {
            System.out.println("Student not found!");
        }
    }

    public void generateCourseReport() {
        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine();

        Course course = findCourseByCode(courseCode);
        if (course != null) {
            System.out.println("\n=== COURSE REPORT ===");
            System.out.printf("Code: %s\n", course.getCourseCode());
            System.out.printf("Title: %s\n", course.getCourseTitle());
            System.out.printf("Units: %d\n", course.getUnits());
            System.out.printf("Lecturer: %s\n",
                    course.getLecturer() != null ? course.getLecturer().getName() : "Unassigned");
            System.out.printf("Enrollment: %d students\n", course.getEnrollmentCount());
            System.out.printf("Average Score: %.2f\n", course.getAverageScore());
            System.out.printf("Highest Score: %.2f\n", course.getHighestScore());
            System.out.printf("Lowest Score: %.2f\n", course.getLowestScore());

            System.out.println("\nEnrolled Students:");
            for (Student student : course.getEnrolledStudents()) {
                Double score = course.getStudentScore(student);
                String scoreStr = score != null ? String.format("%.1f", score) : "Not graded";
                System.out.printf("  %s - %s: %s\n",
                        student.getStudentId(), student.getName(), scoreStr);
            }
        } else {
            System.out.println("Course not found!");
        }
    }

    public static void main(String[] args) {
        new Admin().start();
    }
}