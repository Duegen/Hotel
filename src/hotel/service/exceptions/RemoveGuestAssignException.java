package hotel.service.exceptions;

public class RemoveGuestAssignException extends Exception {
	private static final long serialVersionUID = 1L;

	public RemoveGuestAssignException(int guestId) {
		super("Guest with id %d has related bookings and can't be removed".formatted(guestId));
	}

}
