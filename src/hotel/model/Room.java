package hotel.model;

import java.io.Serializable;
import java.util.Objects;

import hotel.service.dto.input.RoomCreateDTO;

public class Room implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private final int roomNumber;
	private int typeId;
	
	public Room(RoomCreateDTO dto) {
		roomNumber = dto.roomNumber();
		typeId = dto.roomTypeId();
	}

	public Room() {
		this.roomNumber = 0;
		
	}

	private Room(Room room) {
		roomNumber = room.roomNumber;
		typeId = room.typeId;
	}

	@Override
	public String toString() {
		return "Room [roomNumber=" + roomNumber + ", typeId=" + typeId + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(roomNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Room other)) 
            return false;
		return roomNumber == other.roomNumber;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public int getType() {
		return typeId;
	}

	public Room copy() {
		return new Room(this);
	}
}
