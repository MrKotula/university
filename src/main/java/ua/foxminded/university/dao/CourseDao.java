package ua.foxminded.university.dao;

import java.util.List;
import ua.foxminded.university.entity.Course;

public interface CourseDao extends Dao<Course, Integer> {
    List<Course> findByStudentId(String userId);
    
    List<Course> getCoursesMissingByStudentId(String userId);
}
