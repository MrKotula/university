package ua.foxminded.university.validator.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.NoArgsConstructor;
import ua.foxminded.university.exceptions.ValidationException;
import ua.foxminded.university.validator.ValidationService;
import ua.foxminded.university.validator.ValidatorGroup;

@ValidationService
@NoArgsConstructor
public class ValidatorGroupImpl implements ValidatorGroup {
    private static final Pattern SPECIAL_GROUP_PATTERN = Pattern.compile("^[A-Z]{2}-\\d{2}$");
    
    @Override
    public void validateGroupName(String groupName) throws ValidationException {
	validationOnSpecialGroupPattern(groupName);
    }
    
    private boolean validationOnSpecialGroupPattern(String text) throws ValidationException {
	Matcher hasSpecial = SPECIAL_GROUP_PATTERN.matcher(text);
	boolean matchFound = hasSpecial.find();
	
	if (!matchFound) {
	    throw new ValidationException("Group name cannot special format for group!");
	}

	return matchFound;
    }
}
