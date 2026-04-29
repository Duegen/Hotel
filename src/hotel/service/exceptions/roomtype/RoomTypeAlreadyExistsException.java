package hotel.service.exceptions.roomtype;

import hotel.service.exceptions.RoomTypeException;

public class RoomTypeAlreadyExistsException extends RoomTypeException {
	private static final long serialVersionUID = 1L;

	public RoomTypeAlreadyExistsException(int roomTypeId) {
		super("room type with id %d is already existsts".formatted(roomTypeId));
	}

}
