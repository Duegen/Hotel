package hotel.service.exceptions;

public class RemoveRoomTypeAssignException extends Exception {
	private static final long serialVersionUID = 1L;

	public RemoveRoomTypeAssignException(int roomTypeId) {
		super("room type with id %d has related rooms and can't be removed".formatted(roomTypeId));
	}

}
