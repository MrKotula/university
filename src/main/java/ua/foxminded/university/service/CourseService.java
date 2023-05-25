package ua.foxminded.university.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import ua.foxminded.university.entity.Course;
import ua.foxminded.university.exceptions.ValidationException;

public interface CourseService {
    void register(String courseName, String courseDescription) throws ValidationException;
    
    void updateCourseName(String courseId, String courseName) throws ValidationException;
    
    void updateCourseDescription(String courseId, String courseDescription) throws ValidationException;
    
    List<Course> getAllCourses(Pageable pageable);
    
    List<Course> getCoursesForStudentId(String studentId);

    List<Course> getCoursesMissingForStudentId(String studentId);
}
