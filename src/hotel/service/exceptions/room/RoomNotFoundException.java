package hotel.service.exceptions.room;

import hotel.service.exceptions.RoomException;

public class RoomNotFoundException extends RoomException {
	private static final long serialVersionUID = 1L;
	
	public RoomNotFoundException(int roomNumber) {
		super("Room with id %d not found".formatted(roomNumber));
	}
}
