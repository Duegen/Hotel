package hotel.service.exceptions;

public class RoomNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public RoomNotFoundException(int roomNumber) {
		super("Room with id %d not found".formatted(roomNumber));
	}
}
