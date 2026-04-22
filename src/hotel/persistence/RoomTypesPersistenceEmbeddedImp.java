package hotel.persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import hotel.interfaces.IRoomTypesPersistence;
import hotel.model.RoomType;

public class RoomTypesPersistenceEmbeddedImp implements IRoomTypesPersistence {
	private static RoomTypesPersistenceEmbeddedImp instance;

	private RoomTypesPersistenceEmbeddedImp() {

	}

	public static RoomTypesPersistenceEmbeddedImp getInstance() {
		if (instance == null)
			instance = new RoomTypesPersistenceEmbeddedImp();
		return instance;
	}

	@Override
	public void saveRoomTypes(List<RoomType> roomTypes) throws IOException {
		if (Objects.isNull(roomTypes))
			throw new IllegalArgumentException("room types list can not be null");
		Path dataFile = Paths.get(Constants.DIR, Constants.ROOMTYPE_FILE);
		Files.createDirectories(dataFile.getParent());
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataFile.toString()))) {
				out.writeObject(roomTypes);
		} catch (IOException e) {
			throw new IOException("saving room types list to file is failed");
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoomType> loadRoomTypes() throws IOException {
		Path dataFile = Paths.get(Constants.DIR, Constants.ROOMTYPE_FILE);
		if (!Files.exists(dataFile))
			throw new FileNotFoundException("room types data file not found");
		if (Files.size(dataFile) == 0)
			throw new IOException("room types data file is empty");
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(dataFile.toFile()))) {
			return (List<RoomType>) in.readObject();
		} catch(IOException | ClassNotFoundException e) {
			throw new IOException("loading room types list from file is failed");
		}
	}
}
