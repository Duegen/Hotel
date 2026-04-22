package hotel.service.exceptions;

import hotel.model.RoomType;

public class RemoveRoomTypeAssignException extends Exception {
	private static final long serialVersionUID = 1L;

	public RemoveRoomTypeAssignException(RoomType roomType) {
		super("room type %s has related rooms and can't be removed".formatted(roomType.toString()));
	}

}
