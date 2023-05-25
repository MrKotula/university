package ua.foxminded.university.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.university.dao.UserDao;
import ua.foxminded.university.entity.User;
import ua.foxminded.university.service.UserServiceImpl;
import ua.foxminded.university.tools.IdProvider;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = { UserDaoImplTest.Initializer.class })
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserDaoImplTest {
    
    @Autowired
    UserServiceImpl userService;
    
    @Autowired
    UserDao userDao;
    
    @Autowired
    IdProvider idProvider;
    
    User testUser = new User("1d95bc79-a549-4d2c-aeb5-3f929aee4321", "Jake", "Lucki", "JakeLucki@gmail.com", "123456@9");
    User testUserSecond = new User("1d95bc79-a549-4d2c-aeb5-3f929aee4321", "Bohdan", "Bilyk", "bohdanBilyk@gmail.com", null);
    
    private final static InputStream systemIn = System.in;
    private final static PrintStream systemOut = System.out;
    private static ByteArrayOutputStream typeOut;
    
    @ClassRule
    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.2")
	    .withDatabaseName("integration-tests-db")
	    .withUsername("sa")
	    .withPassword("sa");

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
	List<User> users = new ArrayList<>();
	users.add(testUser);
	userDao.update(testUser);

	assertEquals(users, userDao.findAll(null));
    }
    
    @Test
    @Transactional
    void verifyUseMethodWhenUseInsertSaveUser() {
	IdProvider mockedIdProvider = mock(IdProvider.class);
	UserDaoImpl mockedUserDao = mock(UserDaoImpl.class);
	when(mockedIdProvider.generateUUID()).thenReturn("55555");
	when(mockedUserDao.save(testUser)).thenReturn(testUser);
	
	userDao.save(testUser);
	
	assertEquals(testUser.getUserId(), userDao.findById("1d95bc79-a549-4d2c-aeb5-3f929aee4321").get().getUserId());
    }
}
