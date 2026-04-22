package hotel.service.exceptions;

public class BookingAlreadyExistsException extends Exception {
	private static final long serialVersionUID = 1L;

	public BookingAlreadyExistsException(int bookingId) {
		super("booking with id %d is already exists".formatted(bookingId));
	}

	
}
