package ua.foxminded.university.exceptions;

public class ValidationException extends Exception {
    public ValidationException() {
	super();
    }

    public ValidationException(String message) {
	super(message);
    }
}
