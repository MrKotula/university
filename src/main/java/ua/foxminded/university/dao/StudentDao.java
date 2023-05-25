package ua.foxminded.university.dao;

import java.util.List;
import ua.foxminded.university.entity.Student;

public interface StudentDao extends Dao<Student, Integer> {
    void addStudentCourse(String studentId, String courseId);

    void removeStudentFromCourse(String studentId, String courseId);

    List<Student> getStudentsWithCourseName(String courseName);
    
    void createStudent(String firstName, String lastName);
}
