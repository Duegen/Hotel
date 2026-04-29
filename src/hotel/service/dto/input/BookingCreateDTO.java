package hotel.service.dto.input;

import java.time.LocalDate;

public record BookingCreateDTO(
	int guestId,
	int roomNumber,
	LocalDate checkIn,
	LocalDate checkOut) {}
