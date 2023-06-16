package ua.foxminded.university.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import ua.foxminded.university.entity.Group;
import ua.foxminded.university.exceptions.ValidationException;

public interface GroupService {
    void register(String groupName) throws ValidationException;
    
    void updateGroupName(String groupeId, String groupName) throws ValidationException;
    
    List<Group> getAllGroups(Pageable pageable);
}
