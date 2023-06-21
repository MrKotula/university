package ua.foxminded.university.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.foxminded.university.dao.GroupDao;
import ua.foxminded.university.entity.Group;
import ua.foxminded.university.exceptions.ValidationException;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.validator.ValidatorGroup;

@Service
public class GroupServiceImpl implements GroupService {
    private static final String PROPERTY_GROUP_UPDATE_GROUP_NAME = "UPDATE schedule.groups SET group_name = ? WHERE group_id = ?";
    
    private ValidatorGroup validatorGroup;
    private GroupDao groupDao;

    @Autowired
    public GroupServiceImpl(ValidatorGroup validatorGroup, GroupDao groupDao) {
	this.validatorGroup = validatorGroup;
	this.groupDao = groupDao;
    }
    
    @Override
    public List<Group> getGroupsWithLessEqualsStudentCount(int studentCount) {
	String squery = "SELECT groups.group_id, groups.group_name, COUNT(student_id) "
		    + "FROM schedule.groups " + "LEFT JOIN schedule.students ON groups.group_id = students.group_id "
		    + "GROUP BY groups.group_id, groups.group_name " + "HAVING COUNT(student_id) <= " + studentCount + " ORDER BY groups.group_id";

	return groupDao.query(squery);
    }
    
    @Override
    public void register(String groupName) throws ValidationException {
	validatorGroup.validateGroupName(groupName);
	groupDao.save(new Group(groupName));
    }
    
    @Override
    public void updateGroupName(String groupeId, String groupName) throws ValidationException {
	validatorGroup.validateGroupName(groupName);
	Object[] params = { groupName, groupeId };
	groupDao.update(PROPERTY_GROUP_UPDATE_GROUP_NAME, params);
    }
    
    @Override
    public List<Group> getAllGroups(Pageable pageable) {
	return groupDao.findAll(pageable);
    } 
}
