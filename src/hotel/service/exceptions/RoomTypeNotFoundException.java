package hotel.service.exceptions;

import hotel.model.RoomType;

public class RoomTypeNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public RoomTypeNotFoundException(RoomType type) {
		super("room type %s not found".formatted(type.toString()));
	}

}
