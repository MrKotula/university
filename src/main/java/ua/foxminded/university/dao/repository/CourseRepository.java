package ua.foxminded.university.dao.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.foxminded.university.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    @Query(value = "SELECT courses.course_id, courses.course_name, courses.course_description "
    	+ "FROM schedule.courses INNER JOIN schedule.students_courses ON courses.course_id = students_courses.course_id "
    	+ "WHERE students_courses.user_id=:userId", nativeQuery = true)
    List<Course> findByStudentId(@Param("userId") String userId);
    
    @Query(value = "SELECT course_id, course_name, course_description FROM schedule.courses c "
    	+ "WHERE NOT EXISTS (SELECT * FROM schedule.students_courses s_c WHERE user_id =:userId AND c.course_id = s_c.course_id)", nativeQuery = true)
    List<Course> getCoursesMissingByStudentId(String userId);
}
