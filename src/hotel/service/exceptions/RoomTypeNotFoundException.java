package hotel.service.exceptions;

public class RoomTypeNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public RoomTypeNotFoundException(int typeId) {
		super("room type with id %d not found".formatted(typeId));
	}

}
