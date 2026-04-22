package hotel.model;

import java.io.Serializable;
import java.util.Objects;

public class Room implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int roomNumber;
	private RoomType type;
	
	public Room(int roomNumber, RoomType type) {
		this.roomNumber = roomNumber;
		this.type = type;
	}

	@Override
	public String toString() {
		return "Room [roomNumber=" + roomNumber + ", type=" + type + "]";
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

	public RoomType getType() {
		return type;
	}
}
