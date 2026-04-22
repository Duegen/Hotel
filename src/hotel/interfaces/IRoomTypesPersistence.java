package hotel.interfaces;

import java.io.IOException;
import java.util.List;

import hotel.model.RoomType;

public interface IRoomTypesPersistence {
	void saveRoomTypes(List<RoomType> roomTypes) throws IOException;
	List<RoomType> loadRoomTypes() throws IOException;
}
