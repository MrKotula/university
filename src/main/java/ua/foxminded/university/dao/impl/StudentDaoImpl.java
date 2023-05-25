package ua.foxminded.university.dao.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.university.dao.StudentDao;
import ua.foxminded.university.entity.Student;

@Transactional
@Repository
public class StudentDaoImpl extends AbstractDaoImpl<Student> implements StudentDao {
    private static final String PROPERTY_STUDENT_ADD = "INSERT INTO schedule.students(student_id, group_id, first_name, last_name) VALUES (?, ?, ?, ?)";
    private static final String PROPERTY_STUDENT_UPDATE = "UPDATE schedule.students SET student_id = ?, group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?";
    private static final String PROPERTY_STUDENT_GET_BY_ID = "SELECT student_id, group_id, first_name, last_name FROM schedule.students WHERE student_id = ";
    private static final String PROPERTY_STUDENT_GET_ALL = "SELECT * FROM schedule.students";
    private static final String PROPERTY_STUDENT_DELETE = "DELETE FROM schedule.students WHERE student_id = ?";
    private static final String PROPERTY_STUDENT_COURSE_ADD = "INSERT INTO schedule.students_courses(student_id, course_id) VALUES (?, ?)";
    private static final String PROPERTY_STUDENT_COURSE_DELETE = "DELETE FROM schedule.students_courses WHERE student_id = ? and course_id = ?";
    private static final String DEFAULT_GROUP_ID = "3c01e6f1-762e-43b8-a6e1-7cf493ce92e2";

    public StudentDaoImpl(JdbcTemplate jdbcTemplate) {
	super(jdbcTemplate, BeanPropertyRowMapper.newInstance(Student.class), PROPERTY_STUDENT_ADD, PROPERTY_STUDENT_GET_BY_ID, PROPERTY_STUDENT_GET_ALL,
		PROPERTY_STUDENT_UPDATE, PROPERTY_STUDENT_DELETE);
    }

    @Override
    public void addStudentCourse(String studentId, String courseId) {
	String sql = PROPERTY_STUDENT_COURSE_ADD;
	Object[] params = {studentId, courseId};
	jdbcTemplate.update(sql, params);
    }

    @Override
    public void removeStudentFromCourse(String studentId, String courseId) {
	String sql = PROPERTY_STUDENT_COURSE_DELETE;
	Object[] params = {studentId, courseId};
	jdbcTemplate.update(sql, params);
    }

    @Override
    public List<Student> getStudentsWithCourseName(String courseName) {
	List<Student> listOfStudents = jdbcTemplate.query("SELECT * FROM schedule.students WHERE student_id IN"
		+ "(SELECT student_id FROM schedule.students_courses WHERE course_id IN "
		+ "(SELECT course_id FROM schedule.courses WHERE course_name = " + "'" + courseName + "'" + "))"
		+ " ORDER BY student_id", new BeanPropertyRowMapper<Student>(Student.class));

	return listOfStudents;
    }
    
    private void addStudentToBase(Student student) {
	save(student);
    }
    
    @Override
    public void createStudent(String firstName, String lastName) {
	Student student = new Student(DEFAULT_GROUP_ID, firstName, lastName);
	addStudentToBase(student);
    }

    @Override
    protected Object[] insertSave(Student entity) {
	Object[] params = {UUID.randomUUID().toString(), entity.getGroupId(), entity.getFirstName(), entity.getLastName()};
	
	return params;
    }

    @Override
    protected Object[] insertUpdate(Student entity) {
	Object[] params = {entity.getStudentId(), entity.getGroupId(), entity.getFirstName(), entity.getLastName(), entity.getStudentId()};
	
	return params;
    }
}
