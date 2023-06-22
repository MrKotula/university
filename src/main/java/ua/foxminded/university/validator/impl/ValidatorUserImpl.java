package ua.foxminded.university.validator.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ua.foxminded.university.exceptions.ValidationException;
import ua.foxminded.university.validator.ValidationService;
import ua.foxminded.university.validator.ValidatorUser;

@ValidationService
@NoArgsConstructor
@Log4j2
public class ValidatorUserImpl implements ValidatorUser {
    private static final int MAX_LENGTH_OF_FIRSTNAME_OR_LASTNAME = 16;
    private static final Pattern SPECIAL = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

    @Override
    public void validateData(String email, String firstName, String lastName) throws ValidationException {
	validateEmail(email);
	validationOnSpecialCharacters(firstName);
	validationOnSpecialCharacters(lastName);

	if (firstName.length() > MAX_LENGTH_OF_FIRSTNAME_OR_LASTNAME || lastName.length() > MAX_LENGTH_OF_FIRSTNAME_OR_LASTNAME) {
	    log.info("First name or last name is has more 16 symbols!");
	    throw new ValidationException("First name or last name is has more 16 symbols!");
	}
    }

    @Override
    public void validateEmail(String email) throws ValidationException {
	if (!email.contains("@")) {
	    log.info("Email is not correct!");
	    throw new ValidationException("Email is not correct!");
	}
    }

    private boolean validationOnSpecialCharacters(String text) throws ValidationException {
	Matcher hasSpecial = SPECIAL.matcher(text);
	boolean matchFound = hasSpecial.find();
	
	if (matchFound) {
	    log.info("Data cannot contain special characters!");
	    throw new ValidationException("Data cannot contain special characters!");
	}

	return matchFound;
    }
}
