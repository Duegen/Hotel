package hotel.service.exceptions;

public class NoFreeGuestIdException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoFreeGuestIdException() {
		super("no free guest ids are found");
	}
}
