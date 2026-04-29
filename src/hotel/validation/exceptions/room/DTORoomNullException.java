package hotel.validation.exceptions.room;

import hotel.validation.exceptions.ValidationException;

public class DTORoomNullException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public DTORoomNullException() {
		super("room validation error: DTO can't be null");
	}
}
