package ua.foxminded.university.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.foxminded.university.dao.CourseDao;
import ua.foxminded.university.entity.Course;
import ua.foxminded.university.exceptions.ValidationException;
import ua.foxminded.university.service.CourseService;
import ua.foxminded.university.validator.ValidatorCourse;

@Service
public class CourseServiceImpl implements CourseService {
    private static final String PROPERTY_COURSE_UPDATE_COURSE_NAME = "UPDATE schedule.courses SET course_name = ? WHERE course_id = ?";
    private static final String PROPERTY_COURSE_UPDATE_COURSE_DESCRIPTION = "UPDATE schedule.courses SET course_description = ? WHERE course_id = ?";
    
    private ValidatorCourse validatorCourse;
    private CourseDao courseDao;

    @Autowired
    public CourseServiceImpl(ValidatorCourse validatorCourse, CourseDao courseDao) {
	this.validatorCourse = validatorCourse;
	this.courseDao = courseDao;
    }
    
    @Override
    public void register(String courseName, String courseDescription) throws ValidationException {
	validatorCourse.validateCourseName(courseName);
	validatorCourse.validateCourseDescription(courseDescription);

	courseDao.save(new Course(courseName, courseDescription));
    }
    
    @Override
    public void updateCourseName(String courseId, String courseName) throws ValidationException {
	validatorCourse.validateCourseName(courseName);
	Object[] params = { courseName, courseId };
	courseDao.update(PROPERTY_COURSE_UPDATE_COURSE_NAME, params);
    }
    
    @Override
    public void updateCourseDescription(String courseId, String courseDescription) throws ValidationException {
	validatorCourse.validateCourseDescription(courseDescription);
	Object[] params = { courseDescription, courseId };
	courseDao.update(PROPERTY_COURSE_UPDATE_COURSE_DESCRIPTION, params);
    }
    
    @Override
    public List<Course> getAllCourses(Pageable pageable) {
	return courseDao.findAll(pageable);
    }   
}
