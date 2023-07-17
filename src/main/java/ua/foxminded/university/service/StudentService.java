package ua.foxminded.university.service;

import java.util.List;
import ua.foxminded.university.dto.UserDto;
import ua.foxminded.university.entity.Student;
import ua.foxminded.university.enums.Status;
import ua.foxminded.university.exceptions.ValidationException;

public interface StudentService {
    void register(String groupId, UserDto userDto) throws ValidationException;
    
    void updateEmail(Student student) throws ValidationException;
    
    void updatePassword(Student student);
    
    void updateStatus(Status status, String studentId);
    
    void changeGroup(String groupId, String studentId);
    
    List<Student> findByCourseName(String courseName);
    
    void addStudentCourse(String studentId, String courseId);
    
    void removeStudentFromCourse(String studentId, String courseId);
    
    void createStudent(String firstName, String lastName);
    
    void deleteById(String id);
}
