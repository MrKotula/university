package ua.foxminded.university.dao.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.university.dao.StudentDao;
import ua.foxminded.university.entity.Student;
import ua.foxminded.university.tools.Status;

@SpringBootTest
@ContextConfiguration(initializers = { StudentDaoImplTest.Initializer.class })
@Testcontainers
class StudentDaoImplTest {

    @Autowired
    StudentDao studentDao;

    Student testStudent = new Student("33c99439-aaf0-4ebd-a07a-bd0c550db4e1", "3c01e6f1-762e-43b8-a6e1-7cf493ce92e2",
	    "John", "Doe", null, null, Status.STUDENT);
    Student testStudentSecond = new Student("33c99439-aaf0-4ebd-a07a-bd0c550d2311",
	    "3c01e6f1-762e-43b8-a6e1-7cf493ce5325", "Test", "Test", null, null, Status.STUDENT);
    Student testStudentThird = new Student("a78640e5-f9cb-4425-8310-0fec88053693", "3c01e6f1-762e-43b8-a6e1-7cf493ce92e2", "Test", "Test", null, "", Status.NEW);
    List<Student> testListStudent = Arrays.asList(testStudent);

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
    void verifyUseMethodWhenUseInsertUpdate() {
	Student testStudent = new Student("33c99439-aaf0-4ebd-a07a-bd0c550db4e1",
		"3c01e6f1-762e-43b8-a6e1-7cf493ce92e2", "John", "Doe", null, "null", Status.STUDENT);
	studentDao.update(testStudent);

	assertEquals(Optional.of(testStudent), studentDao.findById("33c99439-aaf0-4ebd-a07a-bd0c550db4e1"));
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
    void shouldThrowsEmptyResultDataAccessExceptionWhenTableIsEmptyFindById() {
	assertThrows(EmptyResultDataAccessException.class, () -> studentDao.findById("555"));
    }
}
