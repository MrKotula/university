package ua.foxminded.university.service;

import ua.foxminded.university.dto.UserDto;
import ua.foxminded.university.exceptions.ValidationException;
import ua.foxminded.university.tools.Status;

public interface StudentService {
    void register(String groupId, UserDto userDto) throws ValidationException;
    
    void updateEmail(String email, String studentId) throws ValidationException;
    
    void updatePassword(String password, String studentId);
    
    void updateStatus(Status status, String studentId);
    
    void changeGroup(String groupId, String studentId);
}
