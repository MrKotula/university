package ua.foxminded.university.dao.impl;

import java.util.List;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.university.dao.UserDao;
import ua.foxminded.university.entity.User;
import ua.foxminded.university.tools.IdProvider;

@Transactional
@Repository
public class UserDaoImpl extends AbstractDaoImpl<User> implements UserDao { 
    private static final String PROPERTY_USER_ADD = "INSERT INTO schedule.users(user_id, first_name, last_name, email, password) VALUES (?, ?, ?, ?, ?)";
    private static final String PROPERTY_USER_UPDATE = "UPDATE schedule.users SET first_name = ?, last_name = ?, email = ?, password = ? WHERE user_id = ?";
    private static final String PROPERTY_USER_GET_BY_ID = "SELECT user_id, first_name, last_name, email FROM schedule.users WHERE user_id = ";
    private static final String PROPERTY_USER_GET_ALL = "SELECT user_id, first_name, last_name, email FROM schedule.users";
    private static final String PROPERTY_USER_DELETE = "DELETE FROM schedule.users WHERE user_id = ?";

    public UserDaoImpl(JdbcTemplate jdbcTemplate, IdProvider idProvider) {
	super(jdbcTemplate, BeanPropertyRowMapper.newInstance(User.class), idProvider, PROPERTY_USER_ADD, PROPERTY_USER_GET_BY_ID, PROPERTY_USER_GET_ALL,
		PROPERTY_USER_UPDATE, PROPERTY_USER_DELETE);
    }

    @Override
    protected Object[] insertSave(User entity) {
	Object[] params = { idProvider.generateUUID(), entity.getFirstName(), entity.getLastName(),
		entity.getEmail(), entity.getPassword()};

	return params;
    }

    @Override
    protected Object[] insertUpdate(User entity) {
	Object[] params = { entity.getFirstName(), entity.getLastName(), entity.getEmail(),
		entity.getPassword(), entity.getUserId() };

	return params;
    }
    
    @Override
    public void update(String squery, Object[] params) {
	jdbcTemplate.update(squery, params);
    }
    
    @Override
    public List<User> query(String squery) {
 	return jdbcTemplate.query(squery, new BeanPropertyRowMapper<User>(User.class));
    }
}
