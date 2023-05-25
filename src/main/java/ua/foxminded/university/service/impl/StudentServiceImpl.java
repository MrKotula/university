package ua.foxminded.university.service.impl;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import ua.foxminded.university.dao.StudentDao;
import ua.foxminded.university.dto.UserDto;
import ua.foxminded.university.entity.Student;
import ua.foxminded.university.exceptions.ValidationException;
import ua.foxminded.university.service.StudentService;
import ua.foxminded.university.tools.Status;
import ua.foxminded.university.validator.ValidatorUser;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {
    private static final String DEFAULT_GROUP_ID = "3c01e6f1-762e-43b8-a6e1-7cf493ce92e2";
    private static final String PROPERTY_STUDENT_UPDATE_EMAIL = "UPDATE schedule.students SET email = ? WHERE user_id = ?";
    private static final String PROPERTY_STUDENT_UPDATE_PASSWORD = "UPDATE schedule.students SET password = ? WHERE user_id = ?";
    private static final String PROPERTY_STUDENT_UPDATE_STATUS = "UPDATE schedule.students SET status = ? WHERE user_id = ?";
    private static final String PROPERTY_STUDENT_CHANGE_GROUP = "UPDATE schedule.students SET group_id = ? WHERE user_id = ?";
    private static final String PROPERTY_STUDENT_COURSE_ADD = "INSERT INTO schedule.students_courses(user_id, course_id) VALUES (?, ?)";
    private static final String PROPERTY_STUDENT_COURSE_DELETE = "DELETE FROM schedule.students_courses WHERE user_id = ? and course_id = ?";
    private static final String PROPERTY_STUDENT_DELETE = "DELETE FROM schedule.students WHERE user_id = ?";

    private final ValidatorUser validatorUser;
    private final PasswordEncoder passwordEncoder;
    private final StudentDao studentDao;
    
    @Override
    public List<Student> getStudentsWithCourseName(String courseName) {
	String squery = "SELECT * FROM schedule.students WHERE user_id IN"
		+ "(SELECT user_id FROM schedule.students_courses WHERE course_id IN "
		+ "(SELECT course_id FROM schedule.courses WHERE course_name = " + "'" + courseName + "'" + "))";

	return studentDao.query(squery);
    }
    
    @Override
    public void addStudentCourse(String studentId, String courseId) {
	String sql = PROPERTY_STUDENT_COURSE_ADD;
	Object[] params = {studentId, courseId};
	studentDao.update(sql, params);
    }

    @Override
    public void removeStudentFromCourse(String studentId, String courseId) {
	String sql = PROPERTY_STUDENT_COURSE_DELETE;
	Object[] params = {studentId, courseId};
	studentDao.update(sql, params);
    }
    
    private void addStudentToBase(Student student) {
	studentDao.save(student);
    }
    
    @Override
    public void createStudent(String firstName, String lastName) {
	Student student = new Student(DEFAULT_GROUP_ID, firstName, lastName, Status.STUDENT);
	addStudentToBase(student);
    }
    
    @Override
    public void deleteById(String id) {
	String sql = PROPERTY_STUDENT_DELETE;
	Object[] params = {id};
	studentDao.update(sql, params);
    }

    @Override
    public void register(String groupId, UserDto userDto) throws ValidationException {
	validatorUser.validateData(userDto.getEmail(), userDto.getFirstName(), userDto.getLastName());

	Student student = new Student(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(),
		passwordEncoder.encode(userDto.getPassword()), Status.NEW);
	if (groupId == null) {
	    studentDao.save(student);
	} else {
	    student.setGroupId(groupId);
	    student.setStatus(Status.STUDENT);
	    studentDao.save(student);
	}
    }

    @Override
    public void updateEmail(String email, String studentId) throws ValidationException {
	validatorUser.validateEmail(email);
	Object[] params = { email, studentId };
	studentDao.update(PROPERTY_STUDENT_UPDATE_EMAIL, params);
    }

    @Override
    public void updatePassword(String password, String studentId) {
	Object[] params = { passwordEncoder.encode(password), studentId };
	studentDao.update(PROPERTY_STUDENT_UPDATE_PASSWORD, params);
    }

    @Override
    public void updateStatus(Status status, String studentId) {
	Object[] params = { status.getStatus(), studentId };
	studentDao.update(PROPERTY_STUDENT_UPDATE_STATUS, params);
    }

    @Override
    public void changeGroup(String groupId, String studentId) {
	if (groupId == null) {
	    updateStatus(Status.NEW, studentId);
	} else {
	    updateStatus(Status.STUDENT, studentId);
	}

	Object[] params = { groupId, studentId };
	studentDao.update(PROPERTY_STUDENT_CHANGE_GROUP, params);
    }
}
