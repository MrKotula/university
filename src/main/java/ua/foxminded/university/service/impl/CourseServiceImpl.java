package ua.foxminded.university.service.impl;

import java.util.List;
import org.springframework.data.domain.Pageable;
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
    private static final String PROPERTY_COURSE_UPDATE_COURSE_NAME = "UPDATE schedule.courses SET course_name = ? WHERE course_id = ?";
    private static final String PROPERTY_COURSE_UPDATE_COURSE_DESCRIPTION = "UPDATE schedule.courses SET course_description = ? WHERE course_id = ?";
    
    private final ValidatorCourse validatorCourse;
    private final CourseDao courseDao;
    
    @Override
    public void register(String courseName, String courseDescription) throws ValidationException {
	validatorCourse.validateCourseName(courseName);
	validatorCourse.validateCourseDescription(courseDescription);

	courseDao.save(Course.builder()
		.courseName(courseName)
		.courseDescription(courseDescription)
		.build());
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
