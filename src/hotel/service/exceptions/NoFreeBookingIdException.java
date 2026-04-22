package hotel.service.exceptions;

public class NoFreeBookingIdException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoFreeBookingIdException() {
		super("no free booking ids are found");
	}
	
	
}
