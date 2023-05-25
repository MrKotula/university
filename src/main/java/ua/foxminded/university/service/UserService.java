package ua.foxminded.university.service;

import ua.foxminded.university.dto.UserDto;
import ua.foxminded.university.exceptions.ValidationException;

public interface UserService {
    void register(UserDto userDto) throws ValidationException;
    
    public void updateEmail(String email, String userId) throws ValidationException;
    
    public void updatePassword(String password, String userId);
}
