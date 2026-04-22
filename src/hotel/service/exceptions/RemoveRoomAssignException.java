package hotel.service.exceptions;

public class RemoveRoomAssignException extends Exception {
	private static final long serialVersionUID = 1L;

	public RemoveRoomAssignException(int roomNumber) {
		super("room with number %d has related bookings and can't be removed");
	}

}
