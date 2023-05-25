package ua.foxminded.university.dao.impl;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import ua.foxminded.university.dao.StudentDao;
import ua.foxminded.university.entity.Student;
import ua.foxminded.university.tools.IdProvider;

@Transactional
@Repository
public class StudentDaoImpl extends AbstractDaoImpl<Student> implements StudentDao {
    private static final String PROPERTY_STUDENT_UPDATE = "UPDATE Student SET userId=:userId, groupId=:groupId, firstName=:firstName, lastName=:lastName,"
    	+ "email=:email, password=:password, status=:status WHERE userId=:userId";
    private static final String PROPERTY_STUDENT_GET_BY_ID = "SELECT c FROM Student c WHERE c.userId=?1";
    private static final String PROPERTY_STUDENT_GET_ALL = "SELECT c FROM Student c";
    
    public StudentDaoImpl(EntityManager entityManager, IdProvider idProvider) {
	super(entityManager, idProvider, PROPERTY_STUDENT_GET_BY_ID, PROPERTY_STUDENT_GET_ALL);
    }

    @Override
    protected Student insertSave(Student entity) {
	return new Student(idProvider.generateUUID(), entity.getGroupId(), entity.getFirstName(), entity.getLastName(),
		entity.getEmail(), entity.getPassword(), entity.getStatus());
    }
   

    @SuppressWarnings("unchecked")
    @Override
    protected Query<Student> insertUpdate(Student entity) {
	return (Query<Student>) entityManager.createQuery(PROPERTY_STUDENT_UPDATE)
		.setParameter("userId", entity.getUserId())
		.setParameter("groupId", entity.getGroupId())
		.setParameter("firstName", entity.getFirstName())
		.setParameter("lastName", entity.getLastName())
		.setParameter("email", entity.getEmail())
		.setParameter("password", entity.getPassword())
		.setParameter("status", entity.getStatus());
    }
}
