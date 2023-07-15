package ua.foxminded.university.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.university.dao.repository.CourseRepository;
import ua.foxminded.university.entity.Course;
import ua.foxminded.university.exceptions.ValidationException;
import ua.foxminded.university.service.CourseService;

@SpringBootTest
@ContextConfiguration(initializers = { CourseServiceImplTest.Initializer.class })
@Testcontainers
class CourseServiceImplTest {

    @Autowired
    CourseService courseService;
    
    @Autowired
    CourseRepository courseRepository;
    
    Course testCourse = new Course("1d95bc79-a549-4d2c-aeb5-3f929aee5432", "testCourse", "testDescription");
    Course testCourseMath = new Course("1d95bc79-a549-4d2c-aeb5-3f929aee0f22", "math", "course of Mathematics");
    Course testCourseBiology = new Course("1d95bc79-a549-4d2c-aeb5-3f929aee1234",  "biology", "course of Biology");
    Course testCourseChemistry = new Course("1d95bc79-a549-4d2c-aeb5-3f929aee5324", "chemistry", "course of Chemistry");
    Course testCoursePhysics = new Course("1d95bc79-a549-4d2c-aeb5-3f929aee6589", "physics", "course of Physics");
    Course testCoursePhilosophy = new Course("1d95bc79-a549-4d2c-aeb5-3f929aee8999", "philosophy", "course of Philosophy");
    Course testCourseDrawing = new Course("1d95bc79-a549-4d2c-aeb5-3f929aee0096", "drawing", "course of Drawing");
    Course testCourseLiterature = new Course("1d95bc79-a549-4d2c-aeb5-3f929aee1222", "literature", "course of Literature");
    Course testCourseEnglish = new Course("1d95bc79-a549-4d2c-aeb5-3f929aee7658", "English", "course of English");
    Course testCourseGeography = new Course("1d95bc79-a549-4d2c-aeb5-3f929aee3356", "geography", "course of Geography");
    Course testCoursePhysicalTraining = new Course("1d95bc79-a549-4d2c-aeb5-3f929aee0887", "physical training", "course of Physical training");
    List<Course> testListCourses = Arrays.asList(testCourseMath, testCourseBiology);
    List<Course> testListAllCourses = Arrays.asList(testCourseMath, testCourseBiology, testCourseChemistry, testCoursePhysics, testCoursePhilosophy,
	    testCourseDrawing, testCourseLiterature, testCourseEnglish, testCourseGeography, testCoursePhysicalTraining);

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
	Course course = new Course("testCourse", "testDescription");
	courseService.register("testCourse", "testDescription");

	assertEquals(course.getCourseName(), courseRepository.findAll().get(10).getCourseName());
    }
    
    @Test
    @Transactional
    void verifyUseMethodUpdateCourseName() throws ValidationException {
	testCourse = new Course("1d95bc79-a549-4d2c-aeb5-3f929aee0f22", "test", "course of Mathematics");
	courseService.updateCourseName(testCourse);
	
	assertEquals(Optional.of(testCourse), courseRepository.findById("1d95bc79-a549-4d2c-aeb5-3f929aee0f22"));
    }
    
    @Test
    @Transactional
    void verifyUseMethodUpdateCourseDescription() throws ValidationException {
	testCourse = new Course("1d95bc79-a549-4d2c-aeb5-3f929aee0f22", "math", "test");
	courseService.updateCourseDescription(testCourse);
	
	assertEquals(Optional.of(testCourse), courseRepository.findById("1d95bc79-a549-4d2c-aeb5-3f929aee0f22"));
    }
    
    @Test
    @Transactional
    void shouldReturnValidationExceptionWhenCourseNameIsLonger() throws ValidationException {
	String expectedMessage = "Course name is has more 24 symbols!";
	Exception exception = assertThrows(ValidationException.class, () -> courseService.register("TestTestTestTestTestTestT", "test"));
	
	assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @Transactional
    void shouldReturnValidationExceptionWhenCourseDescriptionIsLonger() throws ValidationException {
	String expectedMessage = "Course description is has more 36 symbols!";
	Exception exception = assertThrows(ValidationException.class, () -> courseService.register("Test", "TestTestTestTestTestTestTestTestTestT"));
	
	assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @Transactional
    void shouldReturnValidationExceptionWhenCourseIncludeSpecialCharacters() throws ValidationException {
	String expectedMessage = "Data cannot contain special characters!";
	Exception exception = assertThrows(ValidationException.class, () -> courseService.register("Tes@t", "TestTes@tTestTestTestTestTestTestTestT"));
	
	assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @Transactional
    void shouldReturnListOfCoursesWhenUseFindByStudentId() {
        assertEquals(testListCourses, courseService.findByStudentId("33c99439-aaf0-4ebd-a07a-bd0c550db4e1"));
    }
    
    @Test
    @Transactional
    void shouldReturnListOfCoursesWhenUseGetCoursesMissingByStudentId() {
        assertEquals(testListAllCourses, courseService.getCoursesMissingByStudentId("1d95bc79-a549-4d2c-aeb5-3f929aee1234"));
    }
}
