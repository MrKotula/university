package ua.foxminded.university.dao.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import lombok.AllArgsConstructor;
import ua.foxminded.university.dao.Dao;

@AllArgsConstructor
public abstract class AbstractDaoImpl<T> implements Dao<T, Integer> {
    protected final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<T> rowMapper;
    
    private final String saveQuery;
    private final String findByIdQuery;
    private final String findAllQuery;
    private final String updateQuery;
    private final String deleteByIdQuery;
    
    @Override
    public Optional<T> findById(String id) {
	try {
	   return Optional.of(jdbcTemplate.queryForObject(findByIdQuery + "'" + id + "'",
		rowMapper)); 
	} catch (EmptyResultDataAccessException e) {
	    return Optional.empty();
	}
    }
  
    @Override
    public List<T> findAll(Pageable pageable) {
	return jdbcTemplate.query(findAllQuery, rowMapper);
    }
    
    @Override
    public void deleteById(String id) {
	Object[] params = {id};
	jdbcTemplate.update(deleteByIdQuery, params);
    }
    
    @Override
    public T save(T entity) {
	jdbcTemplate.update(saveQuery, insertSave(entity));
	return entity;
    }
    
    @Override
    public void update(T entity) {
	jdbcTemplate.update(updateQuery, insertUpdate(entity));
    }
    
    protected abstract Object[] insertSave(T entity);

    protected abstract Object[] insertUpdate(T entity);
}
