# 📚 Java CLI Student Management System

A command-line based Student Management System built in Java. This system allows users to manage student records, subjects, and grades with persistence using JSON storage. It's designed for educational use and Java learning.

---

## 🚀 Features

- ✅ Add new students
- ✅ Delete students by ID
- ✅ Find student by ID or name (exact or partial)
- ✅ View all students
- ✅ Update a student's grade per subject
- ✅ View average grade for a student
- ✅ View top performing students
- ✅ Get students enrolled in a specific subject
- ✅ Get average grade per subject
- ✅ Persist data using `student.json` via Jackson

---

## 🛠 Technologies Used

- **Java 17+**
- **Jackson** for JSON serialization/deserialization
- **Java Collections (Map, List, Stream API)**
- **Java CLI (Scanner-based input)**

---

## 🗃 Data Model

### `Student.java`

```java
class Student {
    private int id;
    private String name;
    private Map<String, Double> subjectGrades;
}
```

- Each student has an auto-incrementing id

- subjectGrades stores subject names and grades (e.g., { "Math": 85.0 })
