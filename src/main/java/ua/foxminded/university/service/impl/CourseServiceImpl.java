package ua.foxminded.university.service.impl;

import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import ua.foxminded.university.dao.CourseDao;
import ua.foxminded.university.entity.Course;
import ua.foxminded.university.exceptions.ValidationException;
import ua.foxminded.university.service.CourseService;
import ua.foxminded.university.validator.ValidatorCourse;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final ValidatorCourse validatorCourse;
    private final CourseDao courseDao;
    
    @Override
    public void register(String courseName, String courseDescription) throws ValidationException {
	validatorCourse.validateCourseName(courseName);
	validatorCourse.validateCourseDescription(courseDescription);

	courseDao.save(new Course(courseName, courseDescription));
    }
    
    @Override
    public void updateCourseName(String courseId, String courseName) throws ValidationException {
	validatorCourse.validateCourseName(courseName);
	Course course = courseDao.findById(courseId).get();
	
	courseDao.update(new Course(course.getCourseId(), courseName, course.getCourseDescription()));
    }
    
    @Override
    public void updateCourseDescription(String courseId, String courseDescription) throws ValidationException {
	validatorCourse.validateCourseDescription(courseDescription);
	Course course = courseDao.findById(courseId).get();
	
	courseDao.update(new Course(course.getCourseId(), course.getCourseName(), courseDescription));
    }
}
