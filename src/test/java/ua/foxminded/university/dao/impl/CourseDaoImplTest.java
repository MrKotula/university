package ua.foxminded.university.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.university.dao.CourseDao;
import ua.foxminded.university.entity.Course;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourseDaoImplTest {
    
    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15.2")
	    .withDatabaseName("integration-tests-db")
	    .withUsername("sa")
	    .withPassword("sa");
    
    @Autowired
    CourseDao courseDao;
    
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
    
    @BeforeAll
    static void setUp() {
	typeOut = new ByteArrayOutputStream();
	System.setOut(new PrintStream(typeOut));
	container.start();

	String simulatedUserInput = "0";
	System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));
    }
    
    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.driver-class-name", container::getDriverClassName);
    }

    @AfterAll
    static void tearDown() {
        container.stop();
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    @Test
    @Transactional
    void shouldReturnListOfCoursesWhenUseGetCoursesForStudentId() {
        assertEquals(testListCourses, courseDao.getCoursesForStudentId("33c99439-aaf0-4ebd-a07a-bd0c550db4e1"));
    }
    
    @Test
    @Transactional
    void shouldReturnListOfCoursesWhenUseGetCoursesMissingForStudentId() {
        assertEquals(testListAllCourses, courseDao.getCoursesMissingForStudentId("1d95bc79-a549-4d2c-aeb5-3f929aee1234"));
    }
    
    @Test
    @Transactional
    void verifyUseMethodWhenUseInsertUpdate() {
	List<Course> courses = new ArrayList<>();
	courses.add(testCourseBiology);
	courseDao.update(testCourseBiology);

	assertEquals(courses, courseDao.getCoursesForStudentId("33c99439-aaf0-4ebd-a07a-bd0c550d2311"));
    }
    
    @Test
    @Transactional
    void verifyUseMethodWhenUseInsertSave() {
	Course course = new Course("75da5fb4-235c-4714-8a2e-460c19c3a0b8", "test", "testing");	
	List<Course> testListAllCourses = Arrays.asList(testCourseMath, testCourseBiology, testCourseChemistry, testCoursePhysics,
		    testCoursePhilosophy, testCourseDrawing, testCourseLiterature, testCourseEnglish, testCourseGeography, testCoursePhysicalTraining,
		    course);
	courseDao.save(course);

	assertEquals(testListAllCourses, courseDao.findAll(null));
    }
}
