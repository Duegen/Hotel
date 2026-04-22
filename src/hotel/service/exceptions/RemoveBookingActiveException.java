package hotel.service.exceptions;

import hotel.model.Booking;

public class RemoveBookingActiveException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public RemoveBookingActiveException(Booking booking) {
		super("booking with id %d is active and can't be canceled".formatted(booking));
	}

	
	

}
