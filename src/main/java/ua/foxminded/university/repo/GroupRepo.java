package ua.foxminded.university.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.foxminded.university.entity.Group;

public interface GroupRepo extends JpaRepository<Group, Long> {

}
