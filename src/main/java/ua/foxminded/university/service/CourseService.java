package ua.foxminded.university.service;

import ua.foxminded.university.exceptions.ValidationException;

public interface CourseService {
    void register(String courseName, String courseDescription) throws ValidationException;
    
    void updateCourseName(String courseId, String courseName) throws ValidationException;
    
    void updateCourseDescription(String courseId, String courseDescription) throws ValidationException;
}
