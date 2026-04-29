package hotel.interfaces;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import hotel.model.Booking;

public interface IBookingsPersistence {
	void saveBookings(List<Booking> bookings, Path dataFile) throws IOException;
	List<Booking> loadBookings(Path dataFile) throws IOException;
}
