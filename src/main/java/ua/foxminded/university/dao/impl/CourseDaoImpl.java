package ua.foxminded.university.dao.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import ua.foxminded.university.dao.CourseDao;
import ua.foxminded.university.entity.Course;
import ua.foxminded.university.tools.IdProvider;

@Transactional
@Repository
@SuppressWarnings("unchecked")
public class CourseDaoImpl extends AbstractDaoImpl<Course> implements CourseDao {
    private static final String PROPERTY_COURSE_GET_ALL = "SELECT c FROM Course c";
    private static final String PROPERTY_COURSE_GET_BY_ID = "SELECT c FROM Course c WHERE c.courseId=?1";
    private static final String PROPERTY_COURSE_FIND_BY_STUDENT_ID = "SELECT courses.course_id, courses.course_name, courses.course_description "
	    + "FROM schedule.courses INNER JOIN schedule.students_courses ON courses.course_id = students_courses.course_id "
	    + "WHERE students_courses.user_id=:userId";
    private static final String PROPERTY_COURSE_FIND_MISSING_COURSES = "SELECT course_id, course_name, course_description "
		+ "FROM schedule.courses c WHERE NOT EXISTS (SELECT * FROM schedule.students_courses s_c WHERE user_id =:userId AND c.course_id = s_c.course_id)";

    public CourseDaoImpl(EntityManager entityManager, IdProvider idProvider) {
	super(entityManager, idProvider, PROPERTY_COURSE_GET_BY_ID, PROPERTY_COURSE_GET_ALL);
    }
    
    @Override
    public List<Course> findByStudentId(String userId) {
	return entityManager.createNativeQuery(PROPERTY_COURSE_FIND_BY_STUDENT_ID, Course.class)
		.setParameter("userId", userId)
		.getResultList();
    }

    @Override
    public List<Course> getCoursesMissingByStudentId(String userId) {
	return entityManager.createNativeQuery(PROPERTY_COURSE_FIND_MISSING_COURSES, Course.class)
		.setParameter("userId", userId)
		.getResultList();
    }
}
