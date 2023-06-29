package ua.foxminded.university.dao;

import java.util.List;
import ua.foxminded.university.entity.Student;
import ua.foxminded.university.tools.Status;

public interface StudentDao extends Dao<Student, Integer> {
    List<Student> findByCourseName(String courseName);
    
    void updateStatus(Status status, String userId);
    
    void changeGroup(String groupId, String userId);
    
    void addStudentCourse(String userId, String courseId);
    
    void removeStudentFromCourse(String userId, String courseId);
}
