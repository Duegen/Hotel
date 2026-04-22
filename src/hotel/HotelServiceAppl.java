package hotel;

import java.nio.file.Files;
import java.nio.file.Path;

import hotel.generator.RandomHotelDataGenerator;
import hotel.interfaces.IHotelPersistenceService;
import hotel.persistence.Constants;
import hotel.service.HotelPersistenceService;
import hotel.service.HotelService;
import hotel.utility.Displayer;

public class HotelServiceAppl {

	public static void main(String[] args) throws Exception {
		boolean filesExist = Files.exists(Path.of(Constants.DIR, Constants.ROOMTYPE_FILE))
				&& Files.exists(Path.of(Constants.DIR, Constants.ROOM_FILE))
				&& Files.exists(Path.of(Constants.DIR, Constants.GUEST_FILE))
				&& Files.exists(Path.of(Constants.DIR, Constants.BOOKING_FILE));
		HotelService service = HotelService.getInstance();
		IHotelPersistenceService servicePersistence = HotelPersistenceService.getInstance(service);
		if(filesExist) {
			try {
				servicePersistence.loadHotelData();
				System.out.println("- Hotel data is loaded from file");
			}catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println("service terminated");
			}
		}
		else {
			System.out.println("- Data files not found");
			RandomHotelDataGenerator randomHotelData = new RandomHotelDataGenerator();
			randomHotelData.copy(service);
			System.out.println("- Hotel data is generated");
		}
		System.out.println();
		Displayer.displayRoomTypes(service.getRoomTypes());
		Displayer.displayRooms(service.getRooms());
		Displayer.displayGuests(service.getGuests());
		Displayer.displayBookings(service.getBookings());
			
		Displayer.displayAnalitics(service.getBookings(), service.getRooms().size());
		System.out.println();
		try {
			servicePersistence.saveHotelData();
			System.out.println("- Hotel data is saved to files");
		}catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("service terminated");
		}
	}
}
