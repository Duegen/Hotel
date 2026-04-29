package hotel.validation.exceptions.roomtype;

import java.util.Objects;

import hotel.validation.exceptions.ValidationException;

public class DTORoomTypeNameException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public DTORoomTypeNameException(String name) {
		String message;
		if(Objects.isNull(name))
			message = "room type validation error: name can't be null";
		else
			message = "room type validation error: name can't be blanked";
		super(message);
	}

}
