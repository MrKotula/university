package ua.foxminded.university.dao;

import java.util.List;

import ua.foxminded.university.entity.Course;

public interface CourseDao extends Dao<Course, Integer> {
    List<Course> getCoursesForStudentId(String studentId);

    List<Course> getCoursesMissingForStudentId(String studentId);
}
