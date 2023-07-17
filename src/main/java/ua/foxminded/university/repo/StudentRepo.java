package ua.foxminded.university.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.foxminded.university.entity.Student;

public interface StudentRepo extends JpaRepository<Student, Long> {
    
}
