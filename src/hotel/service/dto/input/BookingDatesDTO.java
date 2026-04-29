package hotel.service.dto.input;

import java.time.LocalDate;

public record BookingDatesDTO(
		LocalDate checkIn,
		LocalDate checkOut) {}
