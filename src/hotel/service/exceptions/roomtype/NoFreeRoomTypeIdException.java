package hotel.service.exceptions.roomtype;

import hotel.service.exceptions.RoomTypeException;

public class NoFreeRoomTypeIdException extends RoomTypeException {
	private static final long serialVersionUID = 1L;

	public NoFreeRoomTypeIdException() {
		super("no free room type ids are found");
	}
}
