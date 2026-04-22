package hotel.service.exceptions;

import hotel.model.RoomType;

public class RoomTypeAlreadyExistsException extends Exception {
	private static final long serialVersionUID = 1L;

	public RoomTypeAlreadyExistsException(RoomType roomType) {
		super("room type %s is already existsts".formatted(roomType.toString()));
	}

}
