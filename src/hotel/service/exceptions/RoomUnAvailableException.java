package hotel.service.exceptions;

import java.time.LocalDate;

public class RoomUnAvailableException extends Exception {
	private static final long serialVersionUID = 1L;

	public RoomUnAvailableException(int roomNumber, LocalDate checkIn, LocalDate checkOut) {
		super("Room with number %d is unavailable on dates %s - %s".formatted(roomNumber, checkIn.toString(), checkOut.toString()));
	}

	
}
