package ua.foxminded.university.dao;

import ua.foxminded.university.entity.User;

public interface UserDao extends Dao<User, Integer> {
    void update(String squery, Object[] params);
}
