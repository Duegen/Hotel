package hotel.interfaces;

import java.io.IOException;
import java.util.List;

import hotel.model.Booking;

public interface IBookingsPersistence {
	void saveBookings(List<Booking> bookings) throws IOException;
	List<Booking> loadBookings() throws IOException;
}
