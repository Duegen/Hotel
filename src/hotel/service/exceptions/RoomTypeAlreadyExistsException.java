package hotel.service.exceptions;

public class RoomTypeAlreadyExistsException extends Exception {
	private static final long serialVersionUID = 1L;

	public RoomTypeAlreadyExistsException(int roomTypeId) {
		super("room type with id %d is already existsts".formatted(roomTypeId));
	}

}
