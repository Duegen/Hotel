package hotel.interfaces;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import hotel.model.Guest;

public interface IGuestsPersistence {
	void saveGuests(List<Guest> guests, Path dataFile) throws IOException;
	List<Guest> loadGuests(Path dataFile) throws IOException;
}
