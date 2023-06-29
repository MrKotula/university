package ua.foxminded.university.dao.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import ua.foxminded.university.dao.StudentDao;
import ua.foxminded.university.entity.Student;
import ua.foxminded.university.tools.IdProvider;
import ua.foxminded.university.tools.Status;

@Transactional
@Repository
@SuppressWarnings("unchecked")
public class StudentDaoImpl extends AbstractDaoImpl<Student> implements StudentDao {
    private static final String PROPERTY_STUDENT_GET_BY_ID = "SELECT c FROM Student c WHERE c.userId=?1";
    private static final String PROPERTY_STUDENT_GET_ALL = "SELECT c FROM Student c";
    private static final String PROPERTY_STUDENT_UPDATE_STATUS = "UPDATE schedule.students SET status =:status WHERE user_id =:userId";
    private static final String PROPERTY_STUDENT_CHANGE_GROUP = "UPDATE schedule.students SET group_id =:groupId WHERE user_id =:userId";
    private static final String PROPERTY_STUDENT_COURSE_ADD = "INSERT INTO schedule.students_courses(relation_id, user_id, course_id) VALUES (:relationId, :userId, :courseId)";
    private static final String PROPERTY_STUDENT_COURSE_DELETE = "DELETE FROM schedule.students_courses WHERE user_id =:userId and course_id =:courseId";
    private static final String PROPERTY_STUDENT_FIND_BY_COURSE_NAME = "SELECT * FROM schedule.students WHERE user_id IN"
		+ "(SELECT user_id FROM schedule.students_courses WHERE course_id IN "
		+ "(SELECT course_id FROM schedule.courses WHERE course_name =:courseName))";
    
    public StudentDaoImpl(EntityManager entityManager, IdProvider idProvider) {
	super(entityManager, idProvider, PROPERTY_STUDENT_GET_BY_ID, PROPERTY_STUDENT_GET_ALL);
    }
    
    @Override
    public List<Student> findByCourseName(String courseName) {
	return entityManager.createNativeQuery(PROPERTY_STUDENT_FIND_BY_COURSE_NAME, Student.class)
		.setParameter("courseName", courseName)
		.getResultList();
    }
    
    @Override
    public void updateStatus(Status status, String userId) {
	entityManager.createNativeQuery(PROPERTY_STUDENT_UPDATE_STATUS, Student.class)
	.setParameter("status", status.getStatus())
	.setParameter("userId", userId)
	.executeUpdate();
    }

    @Override
    public void changeGroup(String groupId, String userId) {
	if (groupId == null) {
	    updateStatus(Status.NEW, userId);
	} else {
	    updateStatus(Status.STUDENT, userId);
	}

	entityManager.createNativeQuery(PROPERTY_STUDENT_CHANGE_GROUP, Student.class)
	.setParameter("groupId", groupId)
	.setParameter("userId", userId)
	.executeUpdate();
    }
    
    @Override
    public void addStudentCourse(String userId, String courseId) {
	entityManager.createNativeQuery(PROPERTY_STUDENT_COURSE_ADD, Student.class)
	.setParameter("relationId", idProvider.generateUUID())
	.setParameter("userId", userId)
	.setParameter("courseId", courseId)
	.executeUpdate();
    }

    @Override
    public void removeStudentFromCourse(String userId, String courseId) {
	entityManager.createNativeQuery(PROPERTY_STUDENT_COURSE_DELETE, Student.class)
	.setParameter("userId", userId)
	.setParameter("courseId", courseId)
	.executeUpdate();
    }
}
