package hotel.service.exceptions.room;

import java.time.LocalDate;

import hotel.service.exceptions.RoomException;

public class RoomUnAvailableException extends RoomException {
	private static final long serialVersionUID = 1L;

	public RoomUnAvailableException(int roomNumber, LocalDate checkIn, LocalDate checkOut) {
		super("Room with number %d is unavailable on dates %s - %s".formatted(roomNumber, checkIn.toString(), checkOut.toString()));
	}

	
}
