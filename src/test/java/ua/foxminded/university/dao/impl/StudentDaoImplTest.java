package ua.foxminded.university.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.ClassRule;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.university.dao.StudentDao;
import ua.foxminded.university.entity.Student;
import ua.foxminded.university.tools.IdProvider;
import ua.foxminded.university.tools.Status;

@SpringBootTest
@ContextConfiguration(initializers = { StudentDaoImplTest.Initializer.class })
@Testcontainers
class StudentDaoImplTest {

    @Autowired
    StudentDao studentDao;
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    Student testStudent = new Student("33c99439-aaf0-4ebd-a07a-bd0c550db4e1", "3c01e6f1-762e-43b8-a6e1-7cf493ce92e2",
	    "John", "Doe", null, null, Status.STUDENT);
    Student testStudentSecond = new Student("33c99439-aaf0-4ebd-a07a-bd0c550d2311",
	    "3c01e6f1-762e-43b8-a6e1-7cf493ce5325", "Test", "Test", null, null, Status.STUDENT);
    Student testStudentThird = new Student("a78640e5-f9cb-4425-8310-0fec88053693", "3c01e6f1-762e-43b8-a6e1-7cf493ce92e2", "Test", "Test", null, "", Status.NEW);
    List<Student> testListStudent = Arrays.asList(testStudent);

    private final static InputStream systemIn = System.in;
    private final static PrintStream systemOut = System.out;
    private static ByteArrayOutputStream typeOut;

    @ClassRule
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
    void shouldReturnListOfStudentsWhenUseGetStudentsWithCourseName() throws SQLException {
	assertEquals(testListStudent, studentDao.getStudentsWithCourseName("math"));
    }

    @Test
    @Transactional
    void shouldReturnListOfStudentsWhenUseRemoveStudentFromCourse() {
	List<Student> emptyList = Collections.emptyList();
	studentDao.removeStudentFromCourse("33c99439-aaf0-4ebd-a07a-bd0c550db4e1",
		"1d95bc79-a549-4d2c-aeb5-3f929aee0f22");

	assertEquals(emptyList, studentDao.getStudentsWithCourseName("math"));
    }

    @Test
    @Transactional
    void verifyUseMethodWhenUseInsertUpdate() {
	Student testStudent = new Student("33c99439-aaf0-4ebd-a07a-bd0c550db4e1",
		"3c01e6f1-762e-43b8-a6e1-7cf493ce92e2", "John", "Doe", null, "null", Status.STUDENT);
	List<Student> students = new ArrayList<>();
	students.add(testStudent);
	studentDao.update(testStudent);

	assertEquals(students, studentDao.getStudentsWithCourseName("math"));
    }

    @Test
    @Transactional
    void verifyUseMethodWhenUseInsertSaveAndAddStudentCourse() {
	IdProvider mockedIdProvider = mock(IdProvider.class);
	when(mockedIdProvider.generateUUID()).thenReturn("33c99439-aaf0-4ebd-a07a-bd0c550db4e1");
	List<Student> testListStudent = Arrays.asList(testStudent);
	studentDao.save(new Student("33c99439-aaf0-4ebd-a07a-bd0c550db4e1", "3c01e6f1-762e-43b8-a6e1-7cf493ce92e2",
		"John", "Doe", "asd@sa", "123140", Status.NEW));
	studentDao.addStudentCourse("33c99439-aaf0-4ebd-a07a-bd0c550db4e1", "1d95bc79-a549-4d2c-aeb5-3f929aee0096");

	assertEquals(testListStudent, studentDao.getStudentsWithCourseName("drawing"));
    }

    @Test
    @Transactional
    void verifyUseMethodWhenUseDeleteById() {
	List<Student> testListStudent = Arrays.asList(new Student("33c99439-aaf0-4ebd-a07a-bd0c550d2311",
		"3c01e6f1-762e-43b8-a6e1-7cf493ce5325", "Jane", "Does", null, null, Status.STUDENT));
	studentDao.deleteById("33c99439-aaf0-4ebd-a07a-bd0c550db4e1");
	Pageable firstPageWithTwoElements = PageRequest.of(0, 2);

	assertEquals(testListStudent, studentDao.findAll(firstPageWithTwoElements));
    }

    @Test
    @Transactional
    void verifyUseMethodWhenUseCreateStudent() {
	Student testStudent = new Student("33c99439-aaf0-4ebd-a07a-bd0c550db4e2",
		"3c01e6f1-762e-43b8-a6e1-7cf493ce92e2", "Test", "Test", null, "null", Status.STUDENT);
	IdProvider mockedIdProvider = mock(IdProvider.class);
	when(mockedIdProvider.generateUUID()).thenReturn("33c99439-aaf0-4ebd-a07a-bd0c550db4e2");
	studentDao = new StudentDaoImpl(jdbcTemplate, mockedIdProvider);
	
	studentDao.createStudent("Test", "Test");

	assertEquals(Optional.of(testStudent), studentDao.findById("33c99439-aaf0-4ebd-a07a-bd0c550db4e2"));
    }

    @Test
    @Transactional
    void shouldReturnOptionalEmptyWhenTableIsEmptyFindById() {
	assertEquals(Optional.empty(), studentDao.findById("555"));
    }
}
