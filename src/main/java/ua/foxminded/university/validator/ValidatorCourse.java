package ua.foxminded.university.validator;

import ua.foxminded.university.exceptions.ValidationException;

public interface ValidatorCourse {
    void validateCourseName(String courseName) throws ValidationException;

    void validateCourseDescription(String courseDescription) throws ValidationException;
}
