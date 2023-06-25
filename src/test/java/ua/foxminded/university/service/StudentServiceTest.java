package ua.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.university.dao.StudentDao;
import ua.foxminded.university.dao.impl.StudentDaoImpl;
import ua.foxminded.university.dto.UserDto;
import ua.foxminded.university.entity.Student;
import ua.foxminded.university.exceptions.ValidationException;
import ua.foxminded.university.service.StudentService;
import ua.foxminded.university.service.impl.StudentServiceImpl;
import ua.foxminded.university.tools.IdProvider;
import ua.foxminded.university.tools.Status;
import ua.foxminded.university.validator.ValidatorUser;

@SpringBootTest
@ContextConfiguration(initializers = { StudentServiceTest.Initializer.class })
@Testcontainers
class StudentServiceTest {

    @Autowired
    StudentService studentService;
    
    @Autowired
    StudentDao studentDao;
    
    @Autowired 
    JdbcTemplate jdbcTemplate;

    @Autowired
    IdProvider idProvider;
    
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

	assertEquals(Optional.of(testStudent), studentDao.findById("33c99439-aaf0-4ebd-a07a-bd0c550db4e1"));
    }
    
    @Test
    @Transactional
    void verifyUseMethodRegisterWhenGroupIdIsNull() throws ValidationException {
	studentService.register(null, new UserDto("John", "Doe", "testemail@ukr.net", "12345678"));

	assertEquals(Optional.of(testStudent), studentDao.findById("33c99439-aaf0-4ebd-a07a-bd0c550db4e1"));
    }
    
    @Test
    @Transactional
    void verifyUseMethodUpdateEmail() throws ValidationException {
	studentService.updateEmail("testemail@ukr.net", "33c99439-aaf0-4ebd-a07a-bd0c550db4e1");

	assertEquals(Optional.of(testStudent), studentDao.findById("33c99439-aaf0-4ebd-a07a-bd0c550db4e1"));
    }
    
    @Test
    @Transactional
    void verifyUseMethodUpdatePassword() {
	studentService.updatePassword("1234", "33c99439-aaf0-4ebd-a07a-bd0c550db4e1");

	assertEquals(Optional.of(testStudent), studentDao.findById("33c99439-aaf0-4ebd-a07a-bd0c550db4e1"));
    }
    
    @Test
    @Transactional
    void verifyUseMethodUpdateStatus() {
	studentService.updateStatus(Status.STUDENT, "33c99439-aaf0-4ebd-a07a-bd0c550db4e1");

	assertEquals(Optional.of(testStudent), studentDao.findById("33c99439-aaf0-4ebd-a07a-bd0c550db4e1"));
    }
    
    @Test
    @Transactional
    void verifyUseMethodUChangeGroupWhenGroupIdIsNull() {
	studentService.changeGroup(null, "33c99439-aaf0-4ebd-a07a-bd0c550db4e1");

	assertEquals(Optional.of(testStudentSecond), studentDao.findById("33c99439-aaf0-4ebd-a07a-bd0c550db4e1"));
    }
    
    @Test
    @Transactional
    void verifyUseMethodUChangeGroup() {
	Student testStudent = new Student("33c99439-aaf0-4ebd-a07a-bd0c550db4e1", "3c01e6f1-762e-43b8-a6e1-7cf493ce5325", "John", "Doe", null, "12345678", Status.STUDENT);
	studentService.changeGroup("3c01e6f1-762e-43b8-a6e1-7cf493ce5325", "33c99439-aaf0-4ebd-a07a-bd0c550db4e1");

	assertEquals(Optional.of(testStudent), studentDao.findById("33c99439-aaf0-4ebd-a07a-bd0c550db4e1"));
    }
    
    @Test
    @Transactional
    void shouldReturnListOfStudentsWhenUseGetStudentsWithCourseName() {
	List<Student> testListStudent = Arrays.asList(new Student("33c99439-aaf0-4ebd-a07a-bd0c550db4e1",
		"3c01e6f1-762e-43b8-a6e1-7cf493ce92e2", "John", "Doe", null, null, Status.STUDENT));
	
	assertEquals(testListStudent, studentService.getStudentsWithCourseName("math"));
    }

    @Test
    @Transactional
    void shouldReturnListOfStudentsWhenUseRemoveStudentFromCourse() {
	List<Student> emptyList = Collections.emptyList();
	studentService.removeStudentFromCourse("33c99439-aaf0-4ebd-a07a-bd0c550db4e1",
		"1d95bc79-a549-4d2c-aeb5-3f929aee0f22");

	assertEquals(emptyList, studentService.getStudentsWithCourseName("math"));
    }
    
    @Test
    @Transactional
    void verifyUseMethodWhenUseInsertSaveAndAddStudentCourse() {
	IdProvider mockedIdProvider = mock(IdProvider.class);
	when(mockedIdProvider.generateUUID()).thenReturn("33c99439-aaf0-4ebd-a07a-bd0c550db4e1");
	List<Student> testListStudent = Arrays.asList(testStudent);
	studentDao.save(new Student("33c99439-aaf0-4ebd-a07a-bd0c550db4e1", "3c01e6f1-762e-43b8-a6e1-7cf493ce92e2",
		"John", "Doe", "asd@sa", "123140", Status.NEW));
	studentService.addStudentCourse("33c99439-aaf0-4ebd-a07a-bd0c550db4e1", "1d95bc79-a549-4d2c-aeb5-3f929aee0096");

	assertEquals(testListStudent, studentService.getStudentsWithCourseName("drawing"));
    }

    @Test
    @Transactional
    void verifyUseMethodWhenUseDeleteById() {
	List<Student> testListStudent = Arrays.asList(new Student("33c99439-aaf0-4ebd-a07a-bd0c550d2311",
		"3c01e6f1-762e-43b8-a6e1-7cf493ce5325", "Jane", "Does", null, null, Status.STUDENT));
	studentService.deleteById("33c99439-aaf0-4ebd-a07a-bd0c550db4e1");
	Pageable firstPageWithTwoElements = PageRequest.of(0, 2);

	assertEquals(testListStudent, studentDao.findAll(firstPageWithTwoElements));
    }

    @Test
    @Transactional
    void verifyUseMethodWhenUseCreateStudent() {
	PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
	ValidatorUser validatorUser = mock(ValidatorUser.class);
	Student testStudent = new Student("33c99439-aaf0-4ebd-a07a-bd0c550db4e2",
		"3c01e6f1-762e-43b8-a6e1-7cf493ce92e2", "Test", "Test", null, "null", Status.STUDENT);
	IdProvider mockedIdProvider = mock(IdProvider.class);
	when(mockedIdProvider.generateUUID()).thenReturn("33c99439-aaf0-4ebd-a07a-bd0c550db4e2");
	studentDao = new StudentDaoImpl(jdbcTemplate, mockedIdProvider);
	studentService = new StudentServiceImpl(validatorUser, passwordEncoder, studentDao);
	
	studentService.createStudent("Test", "Test");

	assertEquals(Optional.of(testStudent), studentDao.findById("33c99439-aaf0-4ebd-a07a-bd0c550db4e2"));
    }
}
