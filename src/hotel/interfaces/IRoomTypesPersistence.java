package hotel.interfaces;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import hotel.model.RoomType;

public interface IRoomTypesPersistence {
	void saveRoomTypes(List<RoomType> roomTypes, Path dataFile) throws IOException;
	List<RoomType> loadRoomTypes(Path dataFile) throws IOException;
}
