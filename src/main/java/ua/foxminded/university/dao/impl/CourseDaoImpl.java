package ua.foxminded.university.dao.impl;

import java.util.List;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.university.dao.CourseDao;
import ua.foxminded.university.entity.Course;
import ua.foxminded.university.tools.IdProvider;

@Transactional
@Repository
public class CourseDaoImpl extends AbstractDaoImpl<Course> implements CourseDao {
    private static final String PROPERTY_COURSE_ADD = "INSERT INTO schedule.courses(course_id, course_name, course_description) VALUES (?, ?, ?)";
    private static final String PROPERTY_COURSE_GET_ALL = "SELECT * FROM schedule.courses";
    private static final String PROPERTY_COURSE_GET_BY_ID = "SELECT * FROM schedule.courses WHERE course_id =";
    private static final String PROPERTY_COURSE_UPDATE = "UPDATE schedule.courses SET course_id = ?, course_name = ?, course_description = ? WHERE course_id = ?";
    private static final String PROPERTY_COURSE_DELETE = "DELETE FROM schedule.courses WHERE course_id = ?";

    public CourseDaoImpl(JdbcTemplate jdbcTemplate, IdProvider idProvider) {
	super(jdbcTemplate, BeanPropertyRowMapper.newInstance(Course.class), idProvider, PROPERTY_COURSE_ADD, PROPERTY_COURSE_GET_BY_ID, PROPERTY_COURSE_GET_ALL,
		PROPERTY_COURSE_UPDATE, PROPERTY_COURSE_DELETE);
    }
    
   @Override
    protected Object[] insertSave(Course entity) {
	Object[] params = {idProvider.generateUUID(), entity.getCourseName(), entity.getCourseDescription()};
	
	return params;
    }
   
   @Override
   protected Object[] insertUpdate(Course entity) {
       Object[] params = {entity.getCourseId(), entity.getCourseName(), entity.getCourseDescription(), entity.getCourseId()};
       
       return params;
   }
   
   @Override
   public void update(String sqlQuery, Object[] params) {
	jdbcTemplate.update(sqlQuery, params);
   }
   
   @Override
   public List<Course> query(String sqlQuery) {
	return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<Course>(Course.class));
   }
}
