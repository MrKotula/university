package ua.foxminded.university.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import ua.foxminded.university.dao.repository.GroupRepository;
import ua.foxminded.university.entity.Group;
import ua.foxminded.university.exceptions.ValidationException;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.validator.ValidatorGroup;

@Service
@AllArgsConstructor
public class GroupServiceImpl implements GroupService {    
    private final ValidatorGroup validatorGroup;
    private final GroupRepository groupRepository;
    
    @Override
    public void register(String groupName) throws ValidationException {
	validatorGroup.validateGroupName(groupName);
	groupRepository.save(new Group(groupName));
    }
    
    @Override
    public void updateGroupName(Group group) throws ValidationException {
	validatorGroup.validateGroupName(group.getGroupName());
	
	groupRepository.save(group);
    }

    @Override
    public List<Group> getGroupsWithLessEqualsStudentCount(int studentCount) {
	return groupRepository.getGroupsWithLessEqualsStudentCount(studentCount);
    } 
}
