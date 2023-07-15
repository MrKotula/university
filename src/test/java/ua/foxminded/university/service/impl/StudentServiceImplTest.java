package ua.foxminded.university.service.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.university.dao.repository.StudentRepository;
import ua.foxminded.university.dto.UserDto;
import ua.foxminded.university.entity.Student;
import ua.foxminded.university.enums.Status;
import ua.foxminded.university.exceptions.ValidationException;
import ua.foxminded.university.service.StudentService;

@SpringBootTest
@ContextConfiguration(initializers = { StudentServiceImplTest.Initializer.class })
@Testcontainers
class StudentServiceImplTest {

    @Autowired
    StudentService studentService;
    
    @Autowired
    StudentRepository studentRepository;
    
    Student testStudent = new Student("33c99439-aaf0-4ebd-a07a-bd0c550db4e1", "3c01e6f1-762e-43b8-a6e1-7cf493ce92e2",
	    "John", "Doe", null, null, Status.STUDENT);
    Student testStudentSecond = new Student("33c99439-aaf0-4ebd-a07a-bd0c550db4e1", null, "John", "Doe", null, null, Status.NEW);

    private final static InputStream systemIn = System.in;
    private final static PrintStream systemOut = System.out;
    private static ByteArrayOutputStream typeOut;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.2")
	    .withDatabaseName("integration-tests-db").withUsername("sa").withPassword("sa");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
	public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
	    TestPropertyValues
		    .of("spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
			    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
			    "spring.datasource.password=" + postgreSQLContainer.getPassword())
		    .applyTo(configurableApplicationContext.getEnvironment());
	}
    }

    @BeforeAll
    static void setUp() {
	typeOut = new ByteArrayOutputStream();
	System.setOut(new PrintStream(typeOut));
	postgreSQLContainer.start();

	String simulatedUserInput = "0";
	System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));
    }

    @AfterAll
    static void tearDown() {
	postgreSQLContainer.stop();
	System.setIn(systemIn);
	System.setOut(systemOut);
    }

    @Test
    @Transactional
    void verifyUseMethodRegister() throws ValidationException {
	studentService.register("3c01e6f1-762e-43b8-a6e1-7cf493ce92e2", new UserDto("John", "Doe", "testemail@ukr.net", "12345678"));

	assertEquals(Optional.of(testStudent), studentRepository.findById("33c99439-aaf0-4ebd-a07a-bd0c550db4e1"));
    }
    
    @Test
    @Transactional
    void verifyUseMethodRegisterWhenGroupIdIsNull() throws ValidationException {
	studentService.register(null, new UserDto("John", "Doe", "testemail@ukr.net", "12345678"));

	assertEquals(Optional.of(testStudent), studentRepository.findById("33c99439-aaf0-4ebd-a07a-bd0c550db4e1"));
    }
    
    @Test
    @Transactional
    void verifyUseMethodUpdateEmail() throws ValidationException {
	Student testStudent = new Student("33c99439-aaf0-4ebd-a07a-bd0c550db4e1", "3c01e6f1-762e-43b8-a6e1-7cf493ce92e2",
		    "John", "Doe", "testemail@ukr.net", null, Status.STUDENT);
	studentService.updateEmail(testStudent);

	assertEquals(Optional.of(testStudent), studentRepository.findById("33c99439-aaf0-4ebd-a07a-bd0c550db4e1"));
    }
    
    @Test
    @Transactional
    void verifyUseMethodUpdatePassword() {
	Student testStudent = new Student("33c99439-aaf0-4ebd-a07a-bd0c550db4e1", "3c01e6f1-762e-43b8-a6e1-7cf493ce92e2",
		    "John", "Doe", "testemail@ukr.net", "1234", Status.STUDENT);
	studentService.updatePassword(testStudent);

	assertEquals(Optional.of(testStudent), studentRepository.findById("33c99439-aaf0-4ebd-a07a-bd0c550db4e1"));
    }
    
    @Test
    @Transactional
    void verifyUseMethodUpdateStatus() {
	studentService.updateStatus(Status.STUDENT, "33c99439-aaf0-4ebd-a07a-bd0c550db4e1");

	assertEquals(Optional.of(testStudent), studentRepository.findById("33c99439-aaf0-4ebd-a07a-bd0c550db4e1"));
    }
    
    @Test
    @Transactional
    void verifyUseMethodUChangeGroup() {
	Student testStudent = new Student("33c99439-aaf0-4ebd-a07a-bd0c550db4e1", "3c01e6f1-762e-43b8-a6e1-7cf493ce5325", "John", "Doe", null, "12345678", Status.STUDENT);
	studentService.changeGroup("3c01e6f1-762e-43b8-a6e1-7cf493ce5325", "33c99439-aaf0-4ebd-a07a-bd0c550db4e1");

	assertEquals(Optional.of(testStudent), studentRepository.findById("33c99439-aaf0-4ebd-a07a-bd0c550db4e1"));
    }
    
    @Test
    @Transactional
    void shouldReturnListOfStudentsWhenUseGetStudentsWithCourseName() {
	List<Student> testListStudent = Arrays.asList(new Student("33c99439-aaf0-4ebd-a07a-bd0c550db4e1",
		"3c01e6f1-762e-43b8-a6e1-7cf493ce92e2", "John", "Doe", null, null, Status.STUDENT));
	
	assertEquals(testListStudent, studentService.findByCourseName("math"));
    }

    @Test
    @Transactional
    void shouldReturnListOfStudentsWhenUseRemoveStudentFromCourse() {
	List<Student> emptyList = Collections.emptyList();
	studentService.removeStudentFromCourse("33c99439-aaf0-4ebd-a07a-bd0c550db4e1",
		"1d95bc79-a549-4d2c-aeb5-3f929aee0f22");

	assertEquals(emptyList, studentService.findByCourseName("math"));
    }
    
    @Test
    @Transactional
    void verifyUseMethodWhenUseInsertSaveAndAddStudentCourse() {
	List<Student> testListStudent = Arrays.asList(testStudent);
	studentRepository.save(new Student("3c01e6f1-762e-43b8-a6e1-7cf493ce92e2", "John", "Doe", "asd@sa", "123140", Status.NEW));
	studentService.addStudentCourse("33c99439-aaf0-4ebd-a07a-bd0c550db4e1", "1d95bc79-a549-4d2c-aeb5-3f929aee0096");

	assertEquals(testListStudent, studentService.findByCourseName("drawing"));
    }

    @Test
    @Transactional
    void verifyUseMethodWhenUseDeleteById() {
	List<Student> testListStudent = Arrays.asList(new Student("33c99439-aaf0-4ebd-a07a-bd0c550d2311",
		"3c01e6f1-762e-43b8-a6e1-7cf493ce5325", "Jane", "Does", null, null, Status.STUDENT));
	studentService.deleteById("33c99439-aaf0-4ebd-a07a-bd0c550db4e1");

	assertEquals(testListStudent, studentRepository.findAll());
    }

    @Test
    @Transactional
    void verifyUseMethodWhenUseCreateStudent() {
	Student student = new Student("3c01e6f1-762e-43b8-a6e1-7cf493ce5325", "Test", "Test", Status.NEW);
	studentService.createStudent("Test", "Test");

	assertEquals(student.getFirstName(), studentRepository.findAll().get(2).getFirstName());
    }
    
    @Test
    @Transactional
    void shouldReturnValidationExceptionWhenFirstNameIsLonger() throws ValidationException {
	String expectedMessage = "First name or last name is has more 16 symbols!";
	Exception exception = assertThrows(ValidationException.class, () -> studentService.register("3c01e6f1-762e-43b8-a6e1-7cf493cehgfd", 
		new UserDto("JohnJohnJohnJohnF", "Doe", "testemail@ukr.net", "12345678")));
	
	assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @Transactional
    void shouldReturnValidationExceptionWhenLastNameIsLonger() throws ValidationException {
	String expectedMessage = "First name or last name is has more 16 symbols!";
	Exception exception = assertThrows(ValidationException.class, () -> studentService.register("3c01e6f1-762e-43b8-a6e1-7cf493cehgfd", 
		new UserDto("John", "DoeDOEDoeDOEDoeDOE", "testemail@ukr.net", "12345678")));
	
	assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @Transactional
    void shouldReturnValidationExceptionWhenNotContainSpecialSymbol() throws ValidationException {
	String expectedMessage = "Email is not correct!";
	Exception exception = assertThrows(ValidationException.class, () -> studentService.register("3c01e6f1-762e-43b8-a6e1-7cf493cehgfd", 
		new UserDto("John", "Doe", "testemailukr.net", "12345678")));
	
	assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @Transactional
    void shouldReturnValidationExceptionWhenDataContainSpecialCharacters() throws ValidationException {
	String expectedMessage = "Data cannot contain special characters!";
	Exception exception = assertThrows(ValidationException.class, () -> studentService.register("3c01e6f1-762e-43b8-a6e1-7cf493cehgfd", 
		new UserDto("Joh@n", "!Doe", "testemail@ukr.net", "12345678")));
	
	assertEquals(expectedMessage, exception.getMessage());
    }
}
