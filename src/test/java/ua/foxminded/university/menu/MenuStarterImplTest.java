package ua.foxminded.university.menu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.foxminded.university.dao.CourseDao;
import ua.foxminded.university.dao.GroupDao;
import ua.foxminded.university.dao.StudentDao;
import ua.foxminded.university.entity.Student;
import ua.foxminded.university.viewprovider.View;
import ua.foxminded.university.viewprovider.ViewProvider;

@ExtendWith(MockitoExtension.class)
class MenuStarterImplTest {
    private static final String TEST_FIRST_NAME = "First Name";
    private static final String TEST_LAST_NAME = "Last Name";
    private static final String MESSAGE_EXCEPTION_NOT_NUMBER = "You inputted not a number. Please input number ";
    private static final String COURSE_NAME = "math";

    @Mock
    GroupDao groupDao;

    @Mock
    StudentDao studentDao;

    @Mock
    CourseDao courseDao;

    @Mock
    View viewProvider;

    @InjectMocks
    private MenuStarterImpl menuStarterImpl;

    @Test
    void findAllStudentsToCourseNameCorrectResult() {
	List<Student> result = Arrays.asList(
		new Student("asd", "John", "Dou"),
		new Student("daw", "Jane", "Does"));
	String courseName = "math";

	when(viewProvider.readInt()).thenReturn(2).thenReturn(0);
	when(viewProvider.read()).thenReturn(courseName);
	when(studentDao.getStudentsWithCourseName(courseName)).thenReturn(result);

	menuStarterImpl.startMenu();

	verify(viewProvider, times(2)).readInt();
	verify(viewProvider, times(1)).read();
	verify(studentDao, times(1)).getStudentsWithCourseName("math");
    }

    @Test
    void ShouldReturnNewStudentWhenAddStudent() {
	String name = "Jane";
	String surname = "Does";

	when(viewProvider.readInt()).thenReturn(3).thenReturn(0);
	when(viewProvider.read()).thenReturn(name).thenReturn(surname);

	menuStarterImpl.startMenu();

	verify(viewProvider, times(2)).readInt();
	verify(viewProvider, times(2)).read();
	verify(studentDao, times(1)).createStudent(name, surname);
    }

    @Test
    void ShouldReturnStudentWhenRemoveStudent() {
	when(viewProvider.readInt()).thenReturn(4).thenReturn(0);
	when(viewProvider.read()).thenReturn("2");
	menuStarterImpl.startMenu();

	verify(viewProvider, times(2)).readInt();
	verify(viewProvider, times(1)).read();
	verify(studentDao, times(1)).deleteById("2");
    }

    @Test
    void ShouldReturnStudentsWhenAddtoCourse() {
	when(viewProvider.readInt()).thenReturn(5).thenReturn(0);
	when(viewProvider.read()).thenReturn("2").thenReturn("1");
	menuStarterImpl.startMenu();

	verify(viewProvider, times(2)).readInt();
	verify(viewProvider, times(2)).read();
	verify(studentDao, times(1)).addStudentCourse("2", "1");
    }

    @Test
    void ShouldReturnStudentWhenRemoveFromCourse() {
	when(viewProvider.readInt()).thenReturn(6).thenReturn(0);
	when(viewProvider.read()).thenReturn("2").thenReturn("1");
	menuStarterImpl.startMenu();

	verify(viewProvider, times(2)).readInt();
	verify(viewProvider, times(2)).read();
	verify(studentDao, times(1)).removeStudentFromCourse("2", "1");
    }

    @Test
    void ShouldReturnDefaultMenuWhenInputWrongNumber() {
	String message = "Incorrect command\n";
	when(viewProvider.readInt()).thenReturn(7).thenReturn(0);

	menuStarterImpl.startMenu();

	verify(viewProvider, times(2)).readInt();
	verify(viewProvider, times(1)).printMessage(message);
    }

    @Test
    void givenStudentCountString_whenGetGroupsWithLessEqualsStudentCount_thenVerifyCallServicesOneTimeFromArgument() {
	when(viewProvider.readInt()).thenReturn(1).thenReturn(0);

	menuStarterImpl.startMenu();
	ViewProvider view = new ViewProvider();
	view.printMessage("25");

	verify(groupDao, times(1)).getGroupsWithLessEqualsStudentCount(0);
    }

    @Test
    void givenStringInput_whenAddStudentToCourse_thenVerifyCallServicesOneTimeFromArgument() {
	when(viewProvider.read()).thenReturn("1", "3");
	
        menuStarterImpl.addStudentToCourse();
        studentDao.addStudentCourse("1", "3");
        
        verify(studentDao, times(2)).addStudentCourse("1", "3");
    }

    @Test
    void givenStudentIdString_whenDeleteById_thenVerifyCallServicesOneTimeFromArgument() {
	when(viewProvider.read()).thenReturn("3");
	
        studentDao.deleteById("3");
        menuStarterImpl.deleteStudentById();
        
        verify(studentDao, times(2)).deleteById("3");
    }

    @Test
    void shouldThrowException_whenDeleteById_thenVerifyCallServicesOneTimeFromArgument() {
	doThrow(new InputMismatchException(MESSAGE_EXCEPTION_NOT_NUMBER)).when(studentDao)
		.deleteById("35");
	Exception exception = assertThrows(InputMismatchException.class,
		() -> studentDao.deleteById("35"));

	assertEquals(MESSAGE_EXCEPTION_NOT_NUMBER, exception.getMessage());
	verify(studentDao, times(1)).deleteById("35");
    }

    @Test
    void givenCourseName_whenGetStudentsWithCourseName_thenVerifyCallServicesOneTimeFromArgument() {
	String input = COURSE_NAME;
	when(viewProvider.read()).thenReturn(input);

	studentDao.getStudentsWithCourseName(input);
	menuStarterImpl.findAllStudentsToCourseName();

	verify(studentDao, times(2)).getStudentsWithCourseName(COURSE_NAME);
    }

    @Test
    void givenStringInput_whenRemoveStudentFromCourse_thenVerifyCallServicesOneTimeFromArgument() {
        when(viewProvider.read()).thenReturn("3").thenReturn("3");
        
        studentDao.removeStudentFromCourse("3", "3");
        menuStarterImpl.removeStudentFromCourse();
        
        verify(studentDao, times(2)).removeStudentFromCourse("3", "3");
    }

    @Test
    void givenStringInput_whenCreateStudent_thenVerifyCallStudentServiceOneTime() {
        when(viewProvider.read()).thenReturn(TEST_FIRST_NAME).thenReturn(TEST_LAST_NAME);
        
        menuStarterImpl.addStudent();
        studentDao.createStudent(TEST_FIRST_NAME, TEST_LAST_NAME);
        
        verify(studentDao, times(2)).createStudent(TEST_FIRST_NAME, TEST_LAST_NAME);
    }
}
