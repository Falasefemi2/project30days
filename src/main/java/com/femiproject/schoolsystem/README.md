# School System

A simple Java console application for managing a school system. It allows administrators to manage students, lecturers, and courses, as well as handle enrollments and grading.

## Features

- Add, remove, and list students, lecturers, and courses
- Enroll students in courses
- Assign lecturers to courses
- Grade students and calculate CGPA
- Generate student and course reports
- Data persistence using JSON files

## Usage

Run the `Admin` class to start the interactive console menu:

```
public static void main(String[] args) {
    new Admin().start();
}
```

Follow the on-screen prompts to manage the school system.

## File Structure

- `Admin.java`: Main entry and admin operations
- `Student.java`: Student model and CGPA logic
- `Lecturer.java`: Lecturer model
- `Course.java`: Course model and enrollment/score logic

---

_For demonstration and educational purposes._
