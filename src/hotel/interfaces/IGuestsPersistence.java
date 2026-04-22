package hotel.interfaces;

import java.io.IOException;
import java.util.List;

import hotel.model.Guest;

public interface IGuestsPersistence {
	void saveGuests(List<Guest> guests) throws IOException;
	List<Guest> loadGuests() throws IOException;
}
