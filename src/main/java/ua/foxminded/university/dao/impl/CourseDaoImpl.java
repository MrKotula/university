package ua.foxminded.university.dao.impl;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import ua.foxminded.university.dao.CourseDao;
import ua.foxminded.university.entity.Course;
import ua.foxminded.university.tools.IdProvider;

@Transactional
@Repository
public class CourseDaoImpl extends AbstractDaoImpl<Course> implements CourseDao {
    private static final String PROPERTY_COURSE_GET_ALL = "SELECT c FROM Course c";
    private static final String PROPERTY_COURSE_GET_BY_ID = "SELECT c FROM Course c WHERE c.courseId=?1";
    private static final String PROPERTY_COURSE_UPDATE = "UPDATE Course SET courseId=:courseId, courseName=:courseName, courseDescription=:courseDescription"
    	+ " WHERE courseId=:courseId";

    public CourseDaoImpl(EntityManager entityManager, IdProvider idProvider) {
	super(entityManager, idProvider, PROPERTY_COURSE_GET_BY_ID, PROPERTY_COURSE_GET_ALL);
    }
    
    @Override
    protected Course insertSave(Course entity) {
	return new Course(idProvider.generateUUID(), entity.getCourseName(), entity.getCourseDescription());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Query<Course> insertUpdate(Course entity) {
	return (Query<Course>) entityManager.createQuery(PROPERTY_COURSE_UPDATE)
		.setParameter("courseId", entity.getCourseId())
		.setParameter("courseName", entity.getCourseName())
		.setParameter("courseDescription", entity.getCourseDescription());
    }
}
