package ua.foxminded.university.validator;

import ua.foxminded.university.exceptions.ValidationException;

public interface ValidatorGroup {
    void validateGroupName(String groupName) throws ValidationException;
}
