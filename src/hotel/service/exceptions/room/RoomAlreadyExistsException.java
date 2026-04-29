package hotel.service.exceptions.room;

import hotel.service.exceptions.RoomException;

public class RoomAlreadyExistsException extends RoomException{
	private static final long serialVersionUID = 1L;

	public RoomAlreadyExistsException(int roomNumber) {
		super("room with number %d is already existsts".formatted(roomNumber));
	}
}
