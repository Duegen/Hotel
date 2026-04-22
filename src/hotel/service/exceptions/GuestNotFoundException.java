package hotel.service.exceptions;

public class GuestNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public GuestNotFoundException(int guestId) {
		super("guest with id %d not found".formatted(guestId));
	}
	

}
