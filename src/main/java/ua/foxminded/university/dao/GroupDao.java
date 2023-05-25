package ua.foxminded.university.dao;

import java.util.List;

import ua.foxminded.university.entity.Group;

public interface GroupDao extends Dao<Group, Integer> {
    List<Group> getGroupsWithLessEqualsStudentCount(int studentCount);
}
