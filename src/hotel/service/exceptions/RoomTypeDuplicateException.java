package hotel.service.exceptions;

import hotel.model.RoomType;

public class RoomTypeDuplicateException extends Exception {
	private static final long serialVersionUID = 1L;

	public RoomTypeDuplicateException(RoomType roomType) {
		super("duplicated room type %s".formatted(roomType.toString2()));
	}
}
