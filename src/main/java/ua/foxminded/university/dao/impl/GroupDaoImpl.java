package ua.foxminded.university.dao.impl;

import java.util.List;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.university.dao.GroupDao;
import ua.foxminded.university.entity.Group;
import ua.foxminded.university.tools.IdProvider;

@Transactional
@Repository
public class GroupDaoImpl extends AbstractDaoImpl<Group> implements GroupDao {
    private static final String PROPERTY_GROUP_ADD = "INSERT INTO schedule.groups(group_id, group_name) VALUES (?, ?)";
    private static final String PROPERTY_GROUP_GET_BY_ID = "SELECT * FROM schedule.groups WHERE group_id =";
    private static final String PROPERTY_GROUP_GET_ALL = "SELECT * FROM schedule.groups";
    private static final String PROPERTY_GROUP_UPDATE = "UPDATE schedule.groups SET group_id = ?, group_name = ? WHERE group_id = ?";
    private static final String PROPERTY_GROUP_DELETE = "DELETE FROM schedule.groups WHERE group_id = ?";
    
    public GroupDaoImpl(JdbcTemplate jdbcTemplate, IdProvider idProvider) {
	super(jdbcTemplate, BeanPropertyRowMapper.newInstance(Group.class), idProvider, PROPERTY_GROUP_ADD, PROPERTY_GROUP_GET_BY_ID, PROPERTY_GROUP_GET_ALL,
		PROPERTY_GROUP_UPDATE, PROPERTY_GROUP_DELETE);
    }

    @Override
    protected Object[] insertSave(Group entity) {
	Object[] params = {idProvider.generateUUID(), entity.getGroupName()};
	
	return params;
    }

    @Override
    protected Object[] insertUpdate(Group entity) {
	Object[] params = {entity.getGroupId(), entity.getGroupName(), entity.getGroupId()};
	
	return params;
    }
    
    @Override
    public void update(String sqlQuery, Object[] params) {
	jdbcTemplate.update(sqlQuery, params);
    }
    
    @Override
    public List<Group> query(String sqlQuery) {
 	return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<Group>(Group.class));
    }
}
