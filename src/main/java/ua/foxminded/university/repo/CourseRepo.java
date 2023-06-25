package ua.foxminded.university.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.foxminded.university.entity.Course;

public interface CourseRepo extends JpaRepository<Course, Long> {

}
