package ua.foxminded.university.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface Dao<E, ID> {
    E save(E entity);

    Optional<E> findById(String id);

    List<E> findAll(Pageable pageable);

    void update(E entity);

    void deleteById(String id);
}
