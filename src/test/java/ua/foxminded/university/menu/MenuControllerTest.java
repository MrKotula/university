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
import ua.foxminded.university.entity.Student;
import ua.foxminded.university.enums.Status;
import ua.foxminded.university.service.CourseService;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.StudentService;
import ua.foxminded.university.viewprovider.ViewProvider;
import ua.foxminded.university.viewprovider.ViewProviderImpl;

@ExtendWith(MockitoExtension.class)
class MenuControllerTest {
    private static final String TEST_FIRST_NAME = "First Name";
    private static final String TEST_LAST_NAME = "Last Name";
    private static final String MESSAGE_EXCEPTION_NOT_NUMBER = "You inputted not a number. Please input number ";
    private static final String COURSE_NAME = "math";

    @Mock
    GroupService groupService;

    @Mock
    StudentService studentService;

    @Mock
    CourseService courseService;

    @Mock
    ViewProvider viewProvider;

    @InjectMocks
    private MenuController menuController;

    @Test
    void findAllStudentsToCourseNameCorrectResult() {
	List<Student> result = Arrays.asList(
		new Student("asd", "as", "John", "Dou", Status.STUDENT),
		new Student("daw", "as", "Jane", "Does", Status.STUDENT));
	String courseName = "math";

	when(viewProvider.readInt()).thenReturn(2).thenReturn(0);
	when(viewProvider.read()).thenReturn(courseName);
	when(studentService.findByCourseName(courseName)).thenReturn(result);

	menuController.startMenu();

	verify(viewProvider, times(2)).readInt();
	verify(viewProvider, times(1)).read();
	verify(studentService, times(1)).findByCourseName("math");
    }

    @Test
    void ShouldReturnNewStudentWhenAddStudent() {
	String name = "Jane";
	String surname = "Does";

	when(viewProvider.readInt()).thenReturn(3).thenReturn(0);
	when(viewProvider.read()).thenReturn(name).thenReturn(surname);

	menuController.startMenu();

	verify(viewProvider, times(2)).readInt();
	verify(viewProvider, times(2)).read();
	verify(studentService, times(1)).createStudent(name, surname);
    }

    @Test
    void ShouldReturnStudentWhenRemoveStudent() {
	when(viewProvider.readInt()).thenReturn(4).thenReturn(0);
	when(viewProvider.read()).thenReturn("2");
	menuController.startMenu();

	verify(viewProvider, times(2)).readInt();
	verify(viewProvider, times(1)).read();
	verify(studentService, times(1)).deleteById("2");
    }

    @Test
    void ShouldReturnStudentsWhenAddtoCourse() {
	when(viewProvider.readInt()).thenReturn(5).thenReturn(0);
	when(viewProvider.read()).thenReturn("2").thenReturn("1");
	menuController.startMenu();

	verify(viewProvider, times(2)).readInt();
	verify(viewProvider, times(2)).read();
	verify(studentService, times(1)).addStudentCourse("2", "1");
    }

    @Test
    void ShouldReturnStudentWhenRemoveFromCourse() {
	when(viewProvider.readInt()).thenReturn(6).thenReturn(0);
	when(viewProvider.read()).thenReturn("2").thenReturn("1");
	menuController.startMenu();

	verify(viewProvider, times(2)).readInt();
	verify(viewProvider, times(2)).read();
	verify(studentService, times(1)).removeStudentFromCourse("2", "1");
    }

    @Test
    void ShouldReturnDefaultMenuWhenInputWrongNumber() {
	String message = "Incorrect command\n";
	when(viewProvider.readInt()).thenReturn(7).thenReturn(0);

	menuController.startMenu();

	verify(viewProvider, times(2)).readInt();
	verify(viewProvider, times(1)).printMessage(message);
    }

    @Test
    void givenStudentCountString_whenGetGroupsWithLessEqualsStudentCount_thenVerifyCallServicesOneTimeFromArgument() {
	when(viewProvider.readInt()).thenReturn(1).thenReturn(0);

	menuController.startMenu();
	ViewProviderImpl view = new ViewProviderImpl();
	view.printMessage("25");

	verify(groupService, times(1)).getGroupsWithLessEqualsStudentCount(0);
    }

    @Test
    void givenStringInput_whenAddStudentToCourse_thenVerifyCallServicesOneTimeFromArgument() {
	when(viewProvider.read()).thenReturn("1", "3");
	
        menuController.addStudentToCourse();
        studentService.addStudentCourse("1", "3");
        
        verify(studentService, times(2)).addStudentCourse("1", "3");
    }

    @Test
    void givenStudentIdString_whenDeleteById_thenVerifyCallServicesOneTimeFromArgument() {
	when(viewProvider.read()).thenReturn("3");
	
        studentService.deleteById("3");
        menuController.deleteStudentById();
        
        verify(studentService, times(2)).deleteById("3");
    }

    @Test
    void shouldThrowException_whenDeleteById_thenVerifyCallServicesOneTimeFromArgument() {
	doThrow(new InputMismatchException(MESSAGE_EXCEPTION_NOT_NUMBER)).when(studentService)
		.deleteById("35");
	Exception exception = assertThrows(InputMismatchException.class,
		() -> studentService.deleteById("35"));

	assertEquals(MESSAGE_EXCEPTION_NOT_NUMBER, exception.getMessage());
	verify(studentService, times(1)).deleteById("35");
    }

    @Test
    void givenCourseName_whenGetStudentsWithCourseName_thenVerifyCallServicesOneTimeFromArgument() {
	String input = COURSE_NAME;
	when(viewProvider.read()).thenReturn(input);

	studentService.findByCourseName(input);
	menuController.findAllStudentsToCourseName();

	verify(studentService, times(2)).findByCourseName(COURSE_NAME);
    }

    @Test
    void givenStringInput_whenRemoveStudentFromCourse_thenVerifyCallServicesOneTimeFromArgument() {
        when(viewProvider.read()).thenReturn("3").thenReturn("3");
        
        studentService.removeStudentFromCourse("3", "3");
        menuController.removeStudentFromCourse();
        
        verify(studentService, times(2)).removeStudentFromCourse("3", "3");
    }

    @Test
    void givenStringInput_whenCreateStudent_thenVerifyCallStudentServiceOneTime() {
        when(viewProvider.read()).thenReturn(TEST_FIRST_NAME).thenReturn(TEST_LAST_NAME);
        
        menuController.addStudent();
        studentService.createStudent(TEST_FIRST_NAME, TEST_LAST_NAME);
        
        verify(studentService, times(2)).createStudent(TEST_FIRST_NAME, TEST_LAST_NAME);
    }
}
