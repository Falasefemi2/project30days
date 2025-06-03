package com.femiproject.studentmanagement;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StudentManagerTest {
    private StudentManager studentManager;
    private Student student1;
    private Student student2;
    private static final String TEST_FILE = "student.json";

    @BeforeEach
    void setUp() {
        // new File(TEST_FILE).delete();

        studentManager = new StudentManager();
        Map<String, Double> grades1 = new HashMap<>();
        grades1.put("Math", 85.0);
        grades1.put("Science", 90.0);
        student1 = new Student("John Doe", grades1);

        Map<String, Double> grades2 = new HashMap<>();
        grades2.put("Math", 95.0);
        grades2.put("Science", 88.0);
        student2 = new Student("Jane Smith", grades2);
    }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    @Test
    void testJsonFilePersistence() {
        studentManager.addStudents(student1);
        studentManager.addStudents(student2);

        StudentManager newManager = new StudentManager();
        List<Student> loadedStudents = newManager.getAllStudents();

        assertEquals(2, loadedStudents.size());
        assertTrue(loadedStudents.stream().anyMatch(s -> s.getName().equals("John Doe")));
        assertTrue(loadedStudents.stream().anyMatch(s -> s.getName().equals("Jane Smith")));
    }

    @Test
    void testJsonFileUpdate() {
        studentManager.addStudents(student1);
        studentManager.updateStudentGrade(student1.getId(), "Math", 95.0);

        StudentManager newManager = new StudentManager();
        Student loadedStudent = newManager.findStudentById(student1.getId());

        assertNotNull(loadedStudent);
        assertEquals(95.0, loadedStudent.getSubjectGrades().get("Math"));
    }

    @Test
    void testJsonFileDelete() {
        studentManager.addStudents(student1);
        studentManager.deleteStudent(student1.getId());
        List<Student> students = studentManager.getAllStudents();
        assertEquals(0, students.size());
    }

    @Test
    void testDeleteStudent() {
        studentManager.addStudents(student1);
        studentManager.deleteStudent(student1.getId());
        assertNull(studentManager.findStudentById(student1.getId()));
    }

    @Test
    void testFindStudentById() {
        studentManager.addStudents(student1);
        Student found = studentManager.findStudentById(student1.getId());
        assertNotNull(found);
        assertEquals(student1.getName(), found.getName());
    }

    @Test
    void testFindStudentByName() {
        studentManager.addStudents(student1);
        Student found = studentManager.findStudentByName("John Doe");
        assertNotNull(found);
        assertEquals(student1.getId(), found.getId());
    }

    @Test
    void testFindStudentsByPartialName() {
        studentManager.addStudents(student1);
        studentManager.addStudents(student2);
        List<Student> found = studentManager.findStudentsByPartialName("John");
        assertEquals(1, found.size());
        assertEquals("John Doe", found.get(0).getName());
    }

    @Test
    void testGetAllStudents() {
        studentManager.addStudents(student1);
        studentManager.addStudents(student2);
        List<Student> students = studentManager.getAllStudents();
        assertEquals(2, students.size());
    }

    @Test
    void testGetAverageGrade() {
        studentManager.addStudents(student1);
        double average = studentManager.getAverageGrade(student1.getId());
        assertEquals(87.5, average);
    }

    @Test
    void testGetAverageGradePerSubject() {
        studentManager.addStudents(student1);
        studentManager.addStudents(student2);
        Map<String, Double> averages = studentManager.getAverageGradePerSubject();
        assertEquals(90.0, averages.get("Math"));
        assertEquals(89.0, averages.get("Science"));
    }

    @Test
    void testGetStudentsBySubject() {
        studentManager.addStudents(student1);
        studentManager.addStudents(student2);
        List<Student> mathStudents = studentManager.getStudentsBySubject("Math");
        assertEquals(2, mathStudents.size());
    }

    @Test
    void testGetTopPerformingStudents() {
        studentManager.addStudents(student1);
        studentManager.addStudents(student2);
        List<Student> topStudents = studentManager.getTopPerformingStudents(1);
        assertEquals(1, topStudents.size());
        assertEquals(student2.getName(), topStudents.get(0).getName());
    }

    @Test
    void testUpdateStudentGrade() {
        studentManager.addStudents(student1);
        boolean updated = studentManager.updateStudentGrade(student1.getId(), "Math", 95.0);
        assertTrue(updated);
        assertEquals(95.0, student1.getSubjectGrades().get("Math"));
    }

    @Test
    void testUpdateStudentName() {
        studentManager.addStudents(student1);
        boolean updated = studentManager.updateStudentName(student1.getId(), "John Updated");
        assertTrue(updated);
        assertEquals("John Updated", student1.getName());
    }
}
