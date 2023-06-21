package ua.foxminded.university.dao.impl;

import java.util.List;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.university.dao.StudentDao;
import ua.foxminded.university.entity.Student;
import ua.foxminded.university.tools.IdProvider;

@Transactional
@Repository
public class StudentDaoImpl extends AbstractDaoImpl<Student> implements StudentDao {
    private static final String PROPERTY_STUDENT_ADD = "INSERT INTO schedule.students(student_id, group_id, first_name, last_name, email, password, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String PROPERTY_STUDENT_UPDATE = "UPDATE schedule.students SET student_id = ?, group_id = ?, first_name = ?, last_name = ?, email = ?, status = ? WHERE student_id = ?";
    private static final String PROPERTY_STUDENT_GET_BY_ID = "SELECT student_id, group_id, first_name, last_name, email, password, status FROM schedule.students WHERE student_id = ";
    private static final String PROPERTY_STUDENT_GET_ALL = "SELECT * FROM schedule.students";
    private static final String PROPERTY_STUDENT_DELETE = "DELETE FROM schedule.students WHERE student_id = ?";

    public StudentDaoImpl(JdbcTemplate jdbcTemplate, IdProvider idProvider) {
	super(jdbcTemplate, BeanPropertyRowMapper.newInstance(Student.class), idProvider, PROPERTY_STUDENT_ADD, PROPERTY_STUDENT_GET_BY_ID, PROPERTY_STUDENT_GET_ALL,
		PROPERTY_STUDENT_UPDATE, PROPERTY_STUDENT_DELETE);
    }

    @Override
    protected Object[] insertSave(Student entity) {
	Object[] params = { idProvider.generateUUID(), entity.getGroupId(), entity.getFirstName(), entity.getLastName(),
		entity.getEmail(), entity.getPassword(), entity.getStatus().getStatus() };

	return params;
    }

    @Override
    protected Object[] insertUpdate(Student entity) {
	Object[] params = {entity.getStudentId(), entity.getGroupId(), entity.getFirstName(), entity.getLastName(),
		entity.getEmail(), entity.getPassword(), entity.getStatus().getStatus()};
	
	return params;
    }

    @Override
    public void update(String squery, Object[] params) {
	jdbcTemplate.update(squery, params);
    }
    
    @Override
    public List<Student> query(String squery) {
 	return jdbcTemplate.query(squery, new BeanPropertyRowMapper<Student>(Student.class));
    }
}
