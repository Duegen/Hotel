package hotel.validation.exceptions.guest;

import java.util.Objects;

import hotel.validation.exceptions.ValidationException;

public class DTOGuestNameException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public DTOGuestNameException(String name) {
		String message;
		if(Objects.isNull(name))
			message = "guest name validation error: guest name can't be null";
		else
			message = "guest name validation error: guest name can't be blancked";
		super(message);
	}
}
