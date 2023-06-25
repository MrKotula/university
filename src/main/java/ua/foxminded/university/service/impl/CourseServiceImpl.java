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
    public List<Course> getCoursesForStudentId(String studentId) {
	String squery = "SELECT courses.course_id, courses.course_name, courses.course_description "
		    + "FROM schedule.courses INNER JOIN schedule.students_courses ON courses.course_id = students_courses.course_id "
		    + "WHERE students_courses.user_id='" + studentId + "'";
	
	return courseDao.query(squery);
    }

    @Override
    public List<Course> getCoursesMissingForStudentId(String studentId) {
	String squery = "SELECT course_id, course_name, course_description "
		    + "FROM schedule.courses c WHERE NOT EXISTS (SELECT * FROM schedule.students_courses s_c WHERE user_id = '"
		    + studentId + "' AND c.course_id = s_c.course_id)";

	return courseDao.query(squery);
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
