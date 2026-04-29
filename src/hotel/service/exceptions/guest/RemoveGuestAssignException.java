package hotel.service.exceptions.guest;

import hotel.service.exceptions.GuestException;

public class RemoveGuestAssignException extends GuestException {
	private static final long serialVersionUID = 1L;

	public RemoveGuestAssignException(int guestId) {
		super("Guest with id %d has related bookings and can't be removed".formatted(guestId));
	}

}
