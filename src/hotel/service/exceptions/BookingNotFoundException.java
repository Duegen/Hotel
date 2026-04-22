package hotel.service.exceptions;

public class BookingNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public BookingNotFoundException(int bookingId) {
		super("booking with id %d not found".formatted(bookingId));
	}
	

}
