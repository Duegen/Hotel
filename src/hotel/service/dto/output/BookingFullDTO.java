package hotel.service.dto.output;

import java.time.LocalDate;

import hotel.model.Guest;
import hotel.model.RoomType;

public record BookingFullDTO(
	int BookingId,
	Guest guest,
	int roomNumber,
	RoomType roomType,
	LocalDate checkIn,
	LocalDate checkOut) {
}
