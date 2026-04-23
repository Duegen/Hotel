package hotel;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import hotel.generator.RandomHotelDataGenerator;
import hotel.interfaces.IHotelPersistenceService;
import hotel.model.Booking;
import hotel.model.Guest;
import hotel.model.Room;
import hotel.model.RoomType;
import hotel.persistence.Constants;
import hotel.service.HotelPersistenceService;
import hotel.service.HotelService;
import hotel.utility.Displayer;

public class HotelServiceAppl {

	public static void main(String[] args) throws Exception {
		try {
			boolean filesExist = Files.exists(Path.of(Constants.DIR, Constants.ROOMTYPE_FILE))
					&& Files.exists(Path.of(Constants.DIR, Constants.ROOM_FILE))
					&& Files.exists(Path.of(Constants.DIR, Constants.GUEST_FILE))
					&& Files.exists(Path.of(Constants.DIR, Constants.BOOKING_FILE));
			HotelService hotelService = HotelService.getInstance();
			IHotelPersistenceService servicePersistence = HotelPersistenceService.getInstance(hotelService);
			if (filesExist) {
				servicePersistence.loadHotelData();
				System.out.println("- Hotel data is loaded from file");
			} else {
				System.out.println("- Data files not found");
				RandomHotelDataGenerator randomHotelData = new RandomHotelDataGenerator();
				randomHotelData.copy(hotelService);
				System.out.println("- Hotel data is generated");
			}
			System.out.println();
			Displayer.displayRoomTypes(hotelService.getRoomTypes().values());
			Displayer.displayRooms(hotelService.getRooms().values());
			Displayer.displayGuests(hotelService.getGuests().values());
			Displayer.displayBookings(hotelService.getBookings().values());
			Displayer.displayAnalitics(hotelService.getBookings().values(), hotelService.getRooms().values());
			
			System.out.println("=".repeat(30));
			Booking firstBooking = hotelService.getBookings().values().stream()
                    .findFirst().orElse(null);
            if (firstBooking != null) {
                System.out.println("Cancel first booking: " + hotelService.cancelBooking(firstBooking.getBookingId()));
                System.out.println("Bookings count after cancellation: " + hotelService.getBookings().size());
            }
            else
            	System.out.println("Booking list is empty");
            System.out.println("=".repeat(30));
            Room removableRoom = hotelService.getRooms().get(100);
            if (removableRoom == null) {
                RoomType roomType = hotelService.getRoomTypes().values().stream().findFirst().orElse(null);
                if (roomType != null) {
                    Room extraRoom = new Room(100, roomType);
                    hotelService.addRoom(extraRoom.getRoomNumber(), extraRoom.getType());
                    System.out.println("Added extra room 100 for removal demo");
                    System.out.println("Remove room 100: " + hotelService.removeRoom(100));
                }
            }
            System.out.println("=".repeat(30));
            Guest firstGuest = hotelService.getGuests()
            		.values().stream().findFirst().orElse(null);
            if(Objects.nonNull(firstGuest)) {
            	System.out.println("Bookings of first " + firstGuest + ":");
            	List<Booking> bookingsOfFirstGuest = hotelService.getBookingsByGuestsId(firstGuest.getId());
            	if(bookingsOfFirstGuest.isEmpty())
            		System.out.println("no suitable bookings are found");
            	else
            		bookingsOfFirstGuest.stream().forEach(System.out::println);
            }
            else
            	System.out.println("Guest list is empty");
            System.out.println("=".repeat(30));
            firstBooking = hotelService.getBookings().values().stream()
                    .findFirst().orElse(null);
            if(Objects.nonNull(firstBooking)) {
            	System.out.println("Booking check in: " + firstBooking.getCheckIn());
            	List<Booking> bookingsOnDate= hotelService.getBookingsStartOn(firstBooking.getCheckIn());
            	System.out.println("Bookings start on " + firstBooking.getCheckIn() + ":");
            	bookingsOnDate.stream().forEach(System.out::println);
            }
            else
            	System.out.println("Booking list is empty");
            System.out.println("=".repeat(30));
			servicePersistence.saveHotelData();
			System.out.println("- Hotel data is saved to files");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("service terminated");
		}
	}
}
