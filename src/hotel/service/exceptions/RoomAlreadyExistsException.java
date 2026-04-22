package hotel.service.exceptions;

public class RoomAlreadyExistsException extends Exception{
	private static final long serialVersionUID = 1L;

	public RoomAlreadyExistsException(int roomNumber) {
		super("room with number %d is already existsts".formatted(roomNumber));
	}
}
