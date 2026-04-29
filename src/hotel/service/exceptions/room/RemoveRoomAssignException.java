package hotel.service.exceptions.room;

import hotel.service.exceptions.RoomException;

public class RemoveRoomAssignException extends RoomException {
	private static final long serialVersionUID = 1L;

	public RemoveRoomAssignException(int roomNumber) {
		super("room with number %d has related bookings and can't be removed");
	}

}
