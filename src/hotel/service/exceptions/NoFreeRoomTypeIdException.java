package hotel.service.exceptions;

public class NoFreeRoomTypeIdException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoFreeRoomTypeIdException() {
		super("no free room type ids are found");
	}
}
