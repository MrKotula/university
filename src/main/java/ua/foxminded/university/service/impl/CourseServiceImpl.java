package ua.foxminded.university.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import ua.foxminded.university.dao.repository.CourseRepository;
import ua.foxminded.university.entity.Course;
import ua.foxminded.university.exceptions.ValidationException;
import ua.foxminded.university.service.CourseService;
import ua.foxminded.university.validator.ValidatorCourse;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final ValidatorCourse validatorCourse;
    private final CourseRepository courseRepository;
    
    @Override
    public void register(String courseName, String courseDescription) throws ValidationException {
	validatorCourse.validateCourseName(courseName);
	validatorCourse.validateCourseDescription(courseDescription);

	courseRepository.save(new Course(courseName, courseDescription));
    }
    
    @Override
    public void updateCourseName(Course course) throws ValidationException {
	validatorCourse.validateCourseName(course.getCourseName());
	
	courseRepository.save(course);
    }
    
    @Override
    public void updateCourseDescription(Course course) throws ValidationException {
	validatorCourse.validateCourseDescription(course.getCourseDescription());
	
	courseRepository.save(course);
    }

    @Override
    public List<Course> findByStudentId(String userId) {
	return courseRepository.findByStudentId(userId);
    }

    @Override
    public List<Course> getCoursesMissingByStudentId(String userId) {
	return courseRepository.getCoursesMissingByStudentId(userId);
    }
}
