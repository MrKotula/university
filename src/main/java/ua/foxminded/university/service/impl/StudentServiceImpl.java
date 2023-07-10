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
   
    private final ValidatorUser validatorUser;
    private final PasswordEncoder passwordEncoder;
    private final StudentDao studentDao;
    
    @Override
    public List<Student> findByCourseName(String courseName) {
	return studentDao.findByCourseName(courseName);
    }
    
    @Override
    public void addStudentCourse(String studentId, String courseId) {
	studentDao.addStudentCourse(studentId, courseId);
    }

    @Override
    public void removeStudentFromCourse(String studentId, String courseId) {
	studentDao.removeStudentFromCourse(studentId, courseId);
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
	studentDao.deleteById(id);
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
    public void updateEmail(Student student) throws ValidationException {
	validatorUser.validateEmail(student.getEmail());
	
	studentDao.update(student);
    }

    @Override
    public void updatePassword(Student student) {
	studentDao.update(student);
    }

    @Override
    public void updateStatus(Status status, String studentId) {
	studentDao.updateStatus(status, studentId);
    }

    @Override
    public void changeGroup(String groupId, String studentId) {
	studentDao.changeGroup(groupId, studentId);
    }
}
