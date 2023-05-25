package ua.foxminded.university.dao.impl;

import java.util.List;
import java.util.Optional;
import org.hibernate.query.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import ua.foxminded.university.dao.Dao;
import ua.foxminded.university.tools.IdProvider;

@SuppressWarnings("unchecked")
@AllArgsConstructor
@Transactional
public abstract class AbstractDaoImpl<T> implements Dao<T, Integer> {
    @PersistenceContext
    protected final EntityManager entityManager;

    protected final IdProvider idProvider;

    private final String findByIdQuery;
    private final String findAllQuery;

    @Override
    public Optional<T> findById(String id) {
	T entity = (T) entityManager.createQuery(findByIdQuery)
		.setParameter(1, id)
		.getSingleResult();

	return Optional.of(entity);
    }

    @Override
    public List<T> findAll(Pageable pageable) {
	return entityManager.createQuery(findAllQuery).getResultList();
    }

    @Override
    public void deleteById(String id) {
	T entity = findById(id).get();

	if (entity != null) {
	    entityManager.remove(entity);
	}
    }

    @Override
    public T save(T entity) {
	insertSave(entity);
	entityManager.persist(entity);

	return entity;
    }

    @Override
    public void update(T entity) {
	insertUpdate(entity);
	entityManager.merge(entity);
    }

    protected abstract T insertSave(T entity);

    protected abstract Query<T> insertUpdate(T entity);
}
