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

import hotel.interfaces.IGuestsPersistence;
import hotel.model.Guest;

public class GuestPersistenceEmbeddedImp implements IGuestsPersistence{
	private static GuestPersistenceEmbeddedImp instance;
	
	private GuestPersistenceEmbeddedImp() {

	}
	
	public static GuestPersistenceEmbeddedImp getInstance() {
		if (instance == null) 
			instance = new GuestPersistenceEmbeddedImp();
		return instance;
	}
	
	@Override
	public void saveGuests(List<Guest> guests) throws IOException {
		if (Objects.isNull(guests))
			throw new IllegalArgumentException("guests list can not be null");
		Path dataFile = Paths.get(Constants.DIR, Constants.GUEST_FILE);
		Files.createDirectories(dataFile.getParent());
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataFile.toString()))){
				out.writeObject(guests);
		}catch(IOException e) {
			throw new IOException("saving guests list to file is failed");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Guest> loadGuests() throws IOException {
		Path dataFile = Paths.get(Constants.DIR, Constants.GUEST_FILE);
		if(!Files.exists(dataFile))
			throw new FileNotFoundException("guests data file not found");
		if(Files.size(dataFile) == 0)
			throw new IOException("guests data file is empty");
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(dataFile.toFile()))) {
			return (List<Guest>) in.readObject();
		}catch(IOException | ClassNotFoundException e) {
			throw new IOException("loading guests list from file is failed");
		}
	}
}
