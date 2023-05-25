package ua.foxminded.university.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.NoArgsConstructor;
import ua.foxminded.university.exceptions.ValidationException;

@ValidationService
@NoArgsConstructor
public class ValidatorUser implements Validator {
    private static final int MAX_LENGTH_OF_FIRSTNAME_OR_LASTNAME = 16;
    private static final Pattern SPECIAL = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

    @Override
    public void validateData(String email, String firstName, String lastName) throws ValidationException {
	validateEmail(email);
	validationOnSpecialCharacters(firstName);
	validationOnSpecialCharacters(lastName);

	if (firstName.length() > MAX_LENGTH_OF_FIRSTNAME_OR_LASTNAME || lastName.length() > MAX_LENGTH_OF_FIRSTNAME_OR_LASTNAME) {
	    throw new ValidationException("First name or last name is has more 16 symbols!");
	}
    }

    @Override
    public void validateEmail(String email) throws ValidationException {
	if (!email.contains("@")) {
	    throw new ValidationException("Email is not correct!");
	}
    }

    private boolean validationOnSpecialCharacters(String text) throws ValidationException {
	Matcher hasSpecial = SPECIAL.matcher(text);
	boolean matchFound = hasSpecial.find();
	
	if (matchFound) {
	    throw new ValidationException("First name or last name cannot contain special characters!");
	}

	return matchFound;
    }
}
