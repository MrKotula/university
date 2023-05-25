package ua.foxminded.university.dao.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.university.dao.CourseDao;
import ua.foxminded.university.entity.Course;

@Transactional
@Repository
public class CourseDaoImpl extends AbstractDaoImpl<Course> implements CourseDao {
    private static final String PROPERTY_COURSE_ADD = "INSERT INTO schedule.courses(course_id, course_name, course_description) VALUES (?, ?, ?)";
    private static final String PROPERTY_COURSE_GET_ALL = "SELECT * FROM schedule.courses";
    private static final String PROPERTY_COURSE_GET_BY_ID = "SELECT * FROM schedule.courses WHERE course_id =";
    private static final String PROPERTY_COURSE_UPDATE = "UPDATE schedule.courses SET course_id = ?, course_name = ?, course_description = ? WHERE course_id = ?";
    private static final String PROPERTY_COURSE_DELETE = "DELETE FROM schedule.courses WHERE course_id = ?";
    
    public CourseDaoImpl(JdbcTemplate jdbcTemplate) {
	super(jdbcTemplate, BeanPropertyRowMapper.newInstance(Course.class), PROPERTY_COURSE_ADD, PROPERTY_COURSE_GET_BY_ID, PROPERTY_COURSE_GET_ALL,
		PROPERTY_COURSE_UPDATE, PROPERTY_COURSE_DELETE);
    }

    @Override
    public List<Course> getCoursesForStudentId(String studentId) {
	List<Course> listOfCourses = jdbcTemplate.query("SELECT courses.course_id, courses.course_name, courses.course_description "
		    + "FROM schedule.courses INNER JOIN schedule.students_courses ON courses.course_id = students_courses.course_id "
		    + "WHERE students_courses.student_id='" + studentId + "'", new BeanPropertyRowMapper<Course>(Course.class));

	return listOfCourses;
    }

    @Override
    public List<Course> getCoursesMissingForStudentId(String studentId) {
	List<Course> listOfCourses = jdbcTemplate.query("SELECT course_id, course_name, course_description "
		    + "FROM schedule.courses c WHERE NOT EXISTS (SELECT * FROM schedule.students_courses s_c WHERE student_id = '"
		    + studentId + "' AND c.course_id = s_c.course_id)", new BeanPropertyRowMapper<Course>(Course.class));

	return listOfCourses;
    }
    
   @Override
    protected Object[] insertSave(Course entity) {
	Object[] params = {UUID.randomUUID().toString(), entity.getCourseName(), entity.getCourseDescription()};
	
	return params;
    }
   
   @Override
   protected Object[] insertUpdate(Course entity) {
       Object[] params = {entity.getCourseId(), entity.getCourseName(), entity.getCourseDescription(), entity.getCourseId()};
       
       return params;
   }
}
