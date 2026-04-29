package hotel.service.dto.output;

import java.time.LocalDate;

public record BookingDTO(
		int BookingId,
		int roomNumber,
		String caregory,
		double pricePerNight,
		int capacity,
		LocalDate checkIn,
		LocalDate checkOut) {

}
