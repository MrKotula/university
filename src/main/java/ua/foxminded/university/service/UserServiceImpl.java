package ua.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.foxminded.university.dao.UserDao;
import ua.foxminded.university.dto.UserDto;
import ua.foxminded.university.entity.User;
import ua.foxminded.university.exceptions.ValidationException;
import ua.foxminded.university.validator.ValidatorUser;

@Service
public class UserServiceImpl implements UserService {
    private static final String PROPERTY_USER_UPDATE_EMAIL = "UPDATE schedule.users SET email = ? WHERE user_id = ?";
    private static final String PROPERTY_USER_UPDATE_PASSWORD = "UPDATE schedule.users SET password = ? WHERE user_id = ?";

    private ValidatorUser validatorUser;
    private PasswordEncoder passwordEncoder;
    private UserDao userDao;

    @Autowired
    public UserServiceImpl(ValidatorUser validatorUser, PasswordEncoder passwordEncoder, UserDao userDao) {
	this.validatorUser = validatorUser;
	this.passwordEncoder = passwordEncoder;
	this.userDao = userDao;
    }

    @Override
    public void updateEmail(String email, String userId) throws ValidationException {
	validatorUser.validateEmail(email);
	Object[] params = { email, userId };
	userDao.update(PROPERTY_USER_UPDATE_EMAIL, params);
    }

    @Override
    public void updatePassword(String password, String userId) {
	Object[] params = { passwordEncoder.encode(password), userId };
	userDao.update(PROPERTY_USER_UPDATE_PASSWORD, params);
    }

    @Override
    public void register(UserDto userDto) throws ValidationException {
	validatorUser.validateData(userDto.getEmail(), userDto.getFirstName(), userDto.getLastName());

	User user = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(),
		passwordEncoder.encode(userDto.getPassword()));
	userDao.save(user);
    }
}
