package ua.foxminded.university.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
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
import ua.foxminded.university.dao.GroupDao;
import ua.foxminded.university.entity.Group;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GroupDaoImplTest {

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15.2")
	    .withDatabaseName("integration-tests-db")
	    .withUsername("sa")
	    .withPassword("sa");
    
    @Autowired
    GroupDao groupDao;
    
    Group testGroupOR = new Group("3c01e6f1-762e-43b8-a6e1-7cf493ce92e2", "OR-41");
    Group testGroupGM = new Group("3c01e6f1-762e-43b8-a6e1-7cf493ce1234", "GM-87");
    Group testGroupXI = new Group("3c01e6f1-762e-43b8-a6e1-7cf493ce5325", "XI-12");
    Group testGroupYT = new Group("3c01e6f1-762e-43b8-a6e1-7cf493ce2356", "YT-16");
    Group testGroupTH = new Group("3c01e6f1-762e-43b8-a6e1-7cf493ce2344", "TH-13");
    Group testGroupTT = new Group("3c01e6f1-762e-43b8-a6e1-7cf493ce1111", "TT-12");
    Group testGroupLG = new Group("3c01e6f1-762e-43b8-a6e1-7cf493ce8906", "LG-55");
    Group testGroupGN = new Group("3c01e6f1-762e-43b8-a6e1-7cf493ce2337", "GN-33");
    Group testGroupGQ = new Group("3c01e6f1-762e-43b8-a6e1-7cf493ce5775", "GQ-22");
    Group testGroupIT = new Group("3c01e6f1-762e-43b8-a6e1-7cf493ce9988", "IT-18");
    List<Group> testListOfGroups = Arrays.asList(testGroupTT, testGroupGM,  testGroupGN, testGroupTH, testGroupYT,
	    testGroupXI, testGroupGQ, testGroupLG, testGroupOR, testGroupIT);
    
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
    void shouldReturnListOfGroupsWhenUseGetGroupsWithLessEqualsStudentCount() {
	testGroupXI.setCount(1);
	testGroupOR.setCount(1);
        assertEquals(testListOfGroups, groupDao.getGroupsWithLessEqualsStudentCount(55));
    }
    
    @Test
    @Transactional
    void verifyUseMethodWhenUseInsertUpdate() {
	groupDao.update(testGroupGM);

	assertEquals(testGroupGM.getGroupName(), groupDao.findById("3c01e6f1-762e-43b8-a6e1-7cf493ce1234").get().getGroupName());
    }
    
    @Test
    @Transactional
    void verifyUseMethodWhenUseInsertSave() {
	Group testGroupHH = new Group("3c01e6f1-762e-43b8-a6e1-7cf493ce9232", "HH-33");
	groupDao.save(testGroupHH);

	assertEquals(testGroupOR.getGroupName(), groupDao.findById("3c01e6f1-762e-43b8-a6e1-7cf493ce92e2").get().getGroupName());
    }
}
