package hotel.interfaces;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import hotel.model.Room;

public interface IRoomsPersistence {
	void saveRooms(List<Room> rooms, Path dataFile) throws IOException;
	List<Room> loadRooms(Path dataFile) throws IOException;
}
