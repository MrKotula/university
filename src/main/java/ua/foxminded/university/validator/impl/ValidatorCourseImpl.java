package ua.foxminded.university.validator.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.NoArgsConstructor;
import ua.foxminded.university.exceptions.ValidationException;
import ua.foxminded.university.validator.ValidationService;
import ua.foxminded.university.validator.ValidatorCourse;

@ValidationService
@NoArgsConstructor
public class ValidatorCourseImpl implements ValidatorCourse {
    private static final int MAX_LENGTH_OF_COURSE_NAME = 24;
    private static final int MAX_LENGTH_OF_COURSE_DESCRIPTION = 36;
    private static final Pattern SPECIAL = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
    
    @Override
    public void validateCourseName(String courseName) throws ValidationException {
	validationOnSpecialCharacters(courseName);

	if (courseName.length() > MAX_LENGTH_OF_COURSE_NAME) {
	    throw new ValidationException("Course name is has more 24 symbols!");
	}
    }
    
    @Override
    public void validateCourseDescription(String courseDescription) throws ValidationException {
	validationOnSpecialCharacters(courseDescription);

	if (courseDescription.length() > MAX_LENGTH_OF_COURSE_DESCRIPTION) {
	    throw new ValidationException("Course description is has more 36 symbols!");
	}
    }
    private boolean validationOnSpecialCharacters(String text) throws ValidationException {
	Matcher hasSpecial = SPECIAL.matcher(text);
	boolean matchFound = hasSpecial.find();
	
	if (matchFound) {
	    throw new ValidationException("Data cannot contain special characters!");
	}

	return matchFound;
    }
}
