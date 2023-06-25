package ua.foxminded.university.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.foxminded.university.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {

}
