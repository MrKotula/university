package ua.foxminded.university.menu;

import java.util.List;
import org.springframework.stereotype.Component;
import lombok.AllArgsConstructor;
import ua.foxminded.university.entity.Course;
import ua.foxminded.university.entity.Group;
import ua.foxminded.university.entity.Student;
import ua.foxminded.university.service.CourseService;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.StudentService;
import ua.foxminded.university.viewprovider.ViewProvider;

@Component
@AllArgsConstructor
public class MenuController {
    private static final String MESSAGE_FIRST_NAME = "Input first name: ";
    private static final String MESSAGE_LAST_NAME = "Input last name: ";
    private static final String MESSAGE_INPUT_COURSE_NAME = "Input course name: ";
    private static final String MESSAGE_INPUT_STUDENT_ID = "Input student_id for add to course: ";
    private static final String MESSAGE_INPUT_COURSE_ID = "Input course_id from list courses for removing student: ";
    private static final String MESSAGE_INPUT_STUDENT_ID_REMOVE = "Input student_id for remove from course: ";
    private static final String MESSAGE_QUIT_APPLICATION = "Quitting application...";
    private static final String STUDENT_WITH_ID = "Student with id ";

    private final StudentService studentService;
    private final CourseService courseService;
    private final GroupService groupService;
    private final ViewProvider viewProvider;

    public void startMenu() {
	boolean isWork = true;
	while (isWork) {
	    viewProvider.printMessage("\nMain menu\n" + "1. Find all groups with less or equals student count\n"
		    + "2. Find all students related to course with given name\n" + "3. Add new student\n"
		    + "4. Delete student by STUDENT_ID\n" + "5. Add a student to the course (from a list)\n"
		    + "6. Remove the student from one of his or her courses\n" + "0. Exit\n"
		    + "Enter a number from the list:\n");
	    int choose = viewProvider.readInt();

	    switch (choose) {
	    case 1:
		findAllGroupsWithStudentCount();
		break;
	    case 2:
		findAllStudentsToCourseName();
		break;
	    case 3:
		addStudent();
		break;
	    case 4:
		deleteStudentById();
		break;
	    case 5:
		addStudentToCourse();
		break;
	    case 6:
		removeStudentFromCourse();
		break;
	    case 0:
		isWork = false;
		viewProvider.printMessage(MESSAGE_QUIT_APPLICATION);
		break;
	    default:
		viewProvider.printMessage("Incorrect command\n");
	    }
	}
    }

    public void findAllGroupsWithStudentCount() {
	viewProvider.printMessage("Input student count: ");
	int studentCount = viewProvider.readInt();

	List<Group> groups = groupService.getGroupsWithLessEqualsStudentCount(studentCount);
	groups.stream().forEach(System.out::println);
    }

    public void findAllStudentsToCourseName() {
	viewProvider.printMessage(MESSAGE_INPUT_COURSE_NAME);
	String courseName = viewProvider.read();

	List<Student> students = studentService.findByCourseName(courseName);
	students.stream().forEach(System.out::println);
    }

    public void addStudent() {
	viewProvider.printMessage(MESSAGE_FIRST_NAME);
	String firstName = viewProvider.read();

	viewProvider.printMessage(MESSAGE_LAST_NAME);
	String lastName = viewProvider.read();
	
	studentService.createStudent(firstName, lastName);
	viewProvider.printMessage("Created new student in base!");
    }

    public void deleteStudentById() {
	viewProvider.printMessage("Input student_id for deleting: ");
	String studentId = viewProvider.read();

	studentService.deleteById(studentId);
	viewProvider.printMessage(STUDENT_WITH_ID + studentId + " removed!");
    }

    public void addStudentToCourse() {
	viewProvider.printMessage(MESSAGE_INPUT_STUDENT_ID);
	String studentId = viewProvider.read();

	List<Course> courses = courseService.getCoursesMissingByStudentId(studentId);
	courses.stream().forEach(System.out::println);
	
	viewProvider.printMessage("Input course_id from list courses for adding student: ");
	String courseId = viewProvider.read();

	studentService.addStudentCourse(studentId, courseId);
	viewProvider.printMessage(STUDENT_WITH_ID + studentId + " added to course " + courseId);
    }

    public void removeStudentFromCourse() {
	viewProvider.printMessage(MESSAGE_INPUT_STUDENT_ID_REMOVE);
	String studentId = viewProvider.read();

	List<Course> courses = courseService.findByStudentId(studentId);
	courses.stream().forEach(System.out::println);

	viewProvider.printMessage(MESSAGE_INPUT_COURSE_ID);
	String courseId = viewProvider.read();

	studentService.removeStudentFromCourse(studentId, courseId);
	viewProvider.printMessage(STUDENT_WITH_ID + studentId + " removed from course " + courseId);
    }
}
