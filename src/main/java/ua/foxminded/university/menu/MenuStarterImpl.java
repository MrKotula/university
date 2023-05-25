package ua.foxminded.university.menu;

import java.util.List;
import org.springframework.stereotype.Repository;
import ua.foxminded.university.dao.CourseDao;
import ua.foxminded.university.dao.GroupDao;
import ua.foxminded.university.dao.StudentDao;
import ua.foxminded.university.entity.Course;
import ua.foxminded.university.entity.Group;
import ua.foxminded.university.entity.Student;
import ua.foxminded.university.viewprovider.View;

@Repository
public class MenuStarterImpl implements MenuStarter {
    private static final String MESSAGE_FIRST_NAME = "Input first name: ";
    private static final String MESSAGE_LAST_NAME = "Input last name: ";
    private static final String MESSAGE_INPUT_COURSE_NAME = "Input course name: ";
    private static final String MESSAGE_INPUT_STUDENT_ID = "Input student_id for add to course: ";
    private static final String MESSAGE_INPUT_COURSE_ID = "Input course_id from list courses for removing student: ";
    private static final String MESSAGE_INPUT_STUDENT_ID_REMOVE = "Input student_id for remove from course: ";
    private static final String MESSAGE_QUIT_APPLICATION = "Quitting application...";
    private static final String STUDENT_WITH_ID = "Student with id ";

    private final StudentDao studentDao;
    private final CourseDao courseDao;
    private final GroupDao groupDao;
    private final View view;

    public MenuStarterImpl(StudentDao studentDao, CourseDao courseDao, GroupDao groupDao, View view) {
	this.studentDao = studentDao;
	this.courseDao = courseDao;
	this.groupDao = groupDao;
	this.view = view;
    }

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

	List<Group> groups = groupDao.getGroupsWithLessEqualsStudentCount(studentCount);
	groups.stream().forEach(System.out::println);
    }

    @Override
    public void findAllStudentsToCourseName() {
	System.out.print(MESSAGE_INPUT_COURSE_NAME);
	String courseName = view.read();

	List<Student> students = studentDao.getStudentsWithCourseName(courseName);
	students.stream().forEach(System.out::println);
    }

    @Override
    public void addStudent() {
	System.out.print(MESSAGE_FIRST_NAME);
	String firstName = view.read();

	System.out.print(MESSAGE_LAST_NAME);
	String lastName = view.read();
	
	studentDao.createStudent(firstName, lastName);	
	System.out.println("Created new student in base!");
    }

    @Override
    public void deleteStudentById() {
	System.out.print("Input student_id for deleting: ");
	String studentId = view.read();

	studentDao.deleteById(studentId);
	System.out.println(STUDENT_WITH_ID + studentId + " removed!");
    }

    @Override
    public void addStudentToCourse() {
	System.out.print(MESSAGE_INPUT_STUDENT_ID);
	String studentId = view.read();

	List<Course> courses = courseDao.getCoursesMissingForStudentId(studentId);
	courses.stream().forEach(System.out::println);
	
	System.out.print("Input course_id from list courses for adding student: ");
	String courseId = view.read();

	studentDao.addStudentCourse(studentId, courseId);
	System.out.println(STUDENT_WITH_ID + studentId + " added to course " + courseId);
    }

    @Override
    public void removeStudentFromCourse() {
	System.out.print(MESSAGE_INPUT_STUDENT_ID_REMOVE);
	String studentId = view.read();

	List<Course> courses = courseDao.getCoursesForStudentId(studentId);
	courses.stream().forEach(System.out::println);

	System.out.print(MESSAGE_INPUT_COURSE_ID);
	String courseId = view.read();

	studentDao.removeStudentFromCourse(studentId, courseId);
	System.out.println(STUDENT_WITH_ID + studentId + " removed from course " + courseId);
    }
}
