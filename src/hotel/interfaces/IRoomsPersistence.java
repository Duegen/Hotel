package hotel.interfaces;

import java.io.IOException;
import java.util.List;

import hotel.model.Room;

public interface IRoomsPersistence {
	void saveRooms(List<Room> rooms) throws IOException;
	List<Room> loadRooms() throws IOException;
}
