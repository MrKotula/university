package ua.foxminded.university.dao.impl;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import ua.foxminded.university.dao.GroupDao;
import ua.foxminded.university.entity.Group;
import ua.foxminded.university.tools.IdProvider;

@Transactional
@Repository
public class GroupDaoImpl extends AbstractDaoImpl<Group> implements GroupDao {
    private static final String PROPERTY_GROUP_GET_BY_ID = "SELECT c FROM Group c WHERE c.groupId=?1";
    private static final String PROPERTY_GROUP_GET_ALL = "SELECT c FROM Group c";
    private static final String PROPERTY_COURSE_UPDATE = "UPDATE Group SET groupId=:groupId, groupName=:groupName WHERE groupId=:groupId";
  
    public GroupDaoImpl(EntityManager entityManager, IdProvider idProvider) {
	super(entityManager, idProvider, PROPERTY_GROUP_GET_BY_ID, PROPERTY_GROUP_GET_ALL);
    }

    @Override
    protected Group insertSave(Group entity) {
	return new Group(idProvider.generateUUID(), entity.getGroupName());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Query<Group> insertUpdate(Group entity) {
	return (Query<Group>) entityManager.createQuery(PROPERTY_COURSE_UPDATE)
		.setParameter("groupId", entity.getGroupId())
		.setParameter("groupName", entity.getGroupName());
    }
}
