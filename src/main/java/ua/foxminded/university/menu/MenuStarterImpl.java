package ua.foxminded.university.menu;

import java.util.List;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import ua.foxminded.university.entity.Course;
import ua.foxminded.university.entity.Group;
import ua.foxminded.university.entity.Student;
import ua.foxminded.university.service.CourseService;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.StudentService;
import ua.foxminded.university.viewprovider.View;

@Repository
@AllArgsConstructor
public class MenuStarterImpl implements MenuStarter {
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
    private final View view;

    @Override
    public void startMenu() {
	boolean isWork = true;
	while (isWork) {
	    view.printMessage("\nMain menu\n" + "1. Find all groups with less or equals student count\n"
		    + "2. Find all students related to course with given name\n" + "3. Add new student\n"
		    + "4. Delete student by STUDENT_ID\n" + "5. Add a student to the course (from a list)\n"
		    + "6. Remove the student from one of his or her courses\n" + "0. Exit\n"
		    + "Enter a number from the list:\n");
	    int choose = view.readInt();

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
		view.printMessage(MESSAGE_QUIT_APPLICATION);
		break;
	    default:
		view.printMessage("Incorrect command\n");
	    }
	}
    }

    @Override
    public void findAllGroupsWithStudentCount() {
	System.out.print("Input student count: ");
	int studentCount = view.readInt();

	List<Group> groups = groupService.getGroupsWithLessEqualsStudentCount(studentCount);
	groups.stream().forEach(System.out::println);
    }

    @Override
    public void findAllStudentsToCourseName() {
	System.out.print(MESSAGE_INPUT_COURSE_NAME);
	String courseName = view.read();

	List<Student> students = studentService.getStudentsWithCourseName(courseName);
	students.stream().forEach(System.out::println);
    }

    @Override
    public void addStudent() {
	System.out.print(MESSAGE_FIRST_NAME);
	String firstName = view.read();

	System.out.print(MESSAGE_LAST_NAME);
	String lastName = view.read();
	
	studentService.createStudent(firstName, lastName);	
	System.out.println("Created new student in base!");
    }

    @Override
    public void deleteStudentById() {
	System.out.print("Input student_id for deleting: ");
	String studentId = view.read();

	studentService.deleteById(studentId);
	System.out.println(STUDENT_WITH_ID + studentId + " removed!");
    }

    @Override
    public void addStudentToCourse() {
	System.out.print(MESSAGE_INPUT_STUDENT_ID);
	String studentId = view.read();

	List<Course> courses = courseService.getCoursesMissingForStudentId(studentId);
	courses.stream().forEach(System.out::println);
	
	System.out.print("Input course_id from list courses for adding student: ");
	String courseId = view.read();

	studentService.addStudentCourse(studentId, courseId);
	System.out.println(STUDENT_WITH_ID + studentId + " added to course " + courseId);
    }

    @Override
    public void removeStudentFromCourse() {
	System.out.print(MESSAGE_INPUT_STUDENT_ID_REMOVE);
	String studentId = view.read();

	List<Course> courses = courseService.getCoursesForStudentId(studentId);
	courses.stream().forEach(System.out::println);

	System.out.print(MESSAGE_INPUT_COURSE_ID);
	String courseId = view.read();

	studentService.removeStudentFromCourse(studentId, courseId);
	System.out.println(STUDENT_WITH_ID + studentId + " removed from course " + courseId);
    }
}
