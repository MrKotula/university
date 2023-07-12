package ua.foxminded.university.dao.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import ua.foxminded.university.dao.GroupDao;
import ua.foxminded.university.entity.Group;

@Transactional
@Repository
@SuppressWarnings("unchecked")
public class GroupDaoImpl extends AbstractDaoImpl<Group> implements GroupDao {
    private static final String PROPERTY_GROUP_GET_BY_ID = "SELECT c FROM Group c WHERE c.groupId=?1";
    private static final String PROPERTY_GROUP_GET_ALL = "SELECT c FROM Group c";
    private static final String PROPERTY_GROUP_COUNT_GROUPS_BY_STUDENT_ID =  "SELECT groups.group_id, groups.group_name, COUNT(user_id) "
	    + "FROM schedule.groups " + "LEFT JOIN schedule.students ON groups.group_id = students.group_id "
	    + "GROUP BY groups.group_id, groups.group_name " + "HAVING COUNT(user_id) <=:studentCount ORDER BY groups.group_id";
  
    public GroupDaoImpl(EntityManager entityManager) {
	super(entityManager, PROPERTY_GROUP_GET_BY_ID, PROPERTY_GROUP_GET_ALL);
    }
    
    @Override
    public List<Group> getGroupsWithLessEqualsStudentCount(int studentCount) {
	return entityManager.createNativeQuery(PROPERTY_GROUP_COUNT_GROUPS_BY_STUDENT_ID, Group.class)
		.setParameter("studentCount", studentCount)
		.getResultList();
    }
}
