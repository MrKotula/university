package ua.foxminded.university.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.foxminded.university.dao.StudentDao;
import ua.foxminded.university.dto.UserDto;
import ua.foxminded.university.entity.Student;
import ua.foxminded.university.exceptions.ValidationException;
import ua.foxminded.university.service.StudentService;
import ua.foxminded.university.tools.Status;
import ua.foxminded.university.validator.ValidatorUser;

@Service
public class StudentServiceImpl implements StudentService {
    private static final String PROPERTY_STUDENT_UPDATE_EMAIL = "UPDATE schedule.students SET email = ? WHERE student_id = ?";
    private static final String PROPERTY_STUDENT_UPDATE_PASSWORD = "UPDATE schedule.students SET password = ? WHERE student_id = ?";
    private static final String PROPERTY_STUDENT_UPDATE_STATUS = "UPDATE schedule.students SET status = ? WHERE student_id = ?";
    private static final String PROPERTY_STUDENT_CHANGE_GROUP = "UPDATE schedule.students SET group_id = ? WHERE student_id = ?";

    private ValidatorUser validatorUser;
    private PasswordEncoder passwordEncoder;
    private StudentDao studentDao;

    @Autowired
    public StudentServiceImpl(ValidatorUser validatorUser, PasswordEncoder passwordEncoder, StudentDao studentDao) {
	this.validatorUser = validatorUser;
	this.passwordEncoder = passwordEncoder;
	this.studentDao = studentDao;
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
