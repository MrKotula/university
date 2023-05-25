package ua.foxminded.university.dao.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.university.dao.UserDao;
import ua.foxminded.university.dao.impl.UserDaoImpl;
import ua.foxminded.university.dto.UserDto;
import ua.foxminded.university.entity.User;
import ua.foxminded.university.exceptions.ValidationException;
import ua.foxminded.university.service.UserServiceImpl;
import ua.foxminded.university.tools.IdProvider;
import ua.foxminded.university.validator.Validator;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = { UserServiceImplTest.Initializer.class })
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserServiceImplTest {

    @Autowired
    UserServiceImpl userService;
    
    @Autowired
    UserDao userDao;
    
    @Autowired
    IdProvider idProvider;
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    Validator validator;
    
    @Autowired
    PasswordEncoder passwordEncoder;

    User testUser = new User("1d95bc79-a549-4d2c-aeb5-3f929aee4321", "Jake", "Lucki", "JakeLucki@gmail.com", "123456@9");
    User testUserSecond = new User("1d95bc79-a549-4d2c-aeb5-3f929aee4321", "Bohdan", "Bilyk", "bohdanBilyk@gmail.com", null);

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
    void verifyUseMethodRegister() throws ValidationException {
	List<User> users = new ArrayList<>();
	IdProvider mockedIdProvider = mock(IdProvider.class);
	UserDao useDao = new UserDaoImpl(jdbcTemplate, mockedIdProvider);
	userService = new UserServiceImpl(validator, passwordEncoder, useDao);
	
	when(mockedIdProvider.generateUUID()).thenReturn("1d95bc79-a549-4d2c-aeb5-3f929aee5563");
	User user = new User("1d95bc79-a549-4d2c-aeb5-3f929aee5563", "TestName", "TestLastName", "testemail@ukr.net",
		"12345678");
	
	users.add(testUserSecond);
	users.add(user);
	userService.register(new UserDto("TestName", "TestLastName", "testemail@ukr.net", "12345678"));

	assertEquals(users, userDao.findAll(null));
    }
    
    @Test
    @Transactional
    void verifyUseMethodUpdateEmail() throws ValidationException {
	User exceptedUser = new User("1d95bc79-a549-4d2c-aeb5-3f929aee4321", "Bohdan", "Bilyk", "email@gmail.com", "123456@9");
	
	userService.updateEmail("email@gmail.com", "1d95bc79-a549-4d2c-aeb5-3f929aee4321");
	
	assertEquals(exceptedUser.getEmail(), userDao.findById("1d95bc79-a549-4d2c-aeb5-3f929aee4321").get().getEmail());
    }
    
    @Test
    @Transactional
    void verifyUseMethodUpdatePassword() {
	User exceptedUser = new User("1d95bc79-a549-4d2c-aeb5-3f929aee4321", "Bohdan", "Bilyk", "bohdanBilyk@gmail.com", "12345678");

	userService.updatePassword("12345678", "1d95bc79-a549-4d2c-aeb5-3f929aee4321");
	
	assertEquals(Optional.of(exceptedUser), userDao.findById("1d95bc79-a549-4d2c-aeb5-3f929aee4321"));
    }
    
    @Test
    @Transactional
    void shouldReturnValidationExceptionWhenUseMethodRegister() throws ValidationException {
	String expectedMessage = "Email is not correct!";
	Exception exception = assertThrows(ValidationException.class, () -> userService.register(new UserDto("TestName", "TestLastName", "testemailukr.net", "12345678")));
	
	assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @Transactional
    void shouldReturnValidationExceptionWhenMore16SymbolsOfName() throws ValidationException {
	String expectedMessage = "First name or last name is has more 16 symbols!";
	Exception exception = assertThrows(ValidationException.class, () -> userService.register(new UserDto("TestNameTestNameW", "TestLastNameTest", "testema@ilukr.net", "12345678")));
	
	assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @Transactional
    void shouldReturnValidationExceptionWhenMore16SymbolsOfSurname() throws ValidationException {
	String expectedMessage = "First name or last name is has more 16 symbols!";
	Exception exception = assertThrows(ValidationException.class, () -> userService.register(new UserDto("TestNameTestName", "TestLastNameTestN", "testema@ilukr.net", "12345678")));
	
	assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @Transactional
    void shouldReturnValidationExceptionWhenNameOrSurnameHaveSpecialSymbols() throws ValidationException {
	String expectedMessage = "First name or last name cannot contain special characters!";
	Exception exception = assertThrows(ValidationException.class, () -> userService.register(new UserDto("TestNameTestNam@", "TestLastNameTes^", "testema@ilukr.net", "12345678")));
	
	assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @Transactional
    void shouldReturnValidationExceptionWhenSurnameHaveSpecialSymbols() throws ValidationException {
	String expectedMessage = "First name or last name cannot contain special characters!";
	Exception exception = assertThrows(ValidationException.class, () -> userService.register(new UserDto("TestNameTestNam", "TestLastNameTes#", "testema@ilukr.net", "12345678")));
	
	assertEquals(expectedMessage, exception.getMessage());
    }
}
