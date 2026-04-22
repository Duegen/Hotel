package hotel.service.exceptions;

public class GuestAlreadyExistsException extends Exception {
	private static final long serialVersionUID = 1L;

	public GuestAlreadyExistsException(int id) {
		super("guest with id %d is already exists".formatted(id));
	}

}
