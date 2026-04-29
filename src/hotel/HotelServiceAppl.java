package hotel;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import cli.ConsoleInputOutput;
import hotel.generator.RandomHotelDataGenerator;
import hotel.interfaces.IHotelPersistenceService;
import hotel.model.Booking;
import hotel.model.Guest;
import hotel.model.Room;
import hotel.model.RoomType;
import hotel.persistence.BookingsPersistenceEmbeddedImp;
import hotel.persistence.GuestsPersistenceEmbeddedImp;
import hotel.persistence.RoomTypesPersistenceEmbeddedImp;
import hotel.persistence.RoomsPersistenceEmbeddedImp;
import hotel.service.HotelPersistenceService;
import hotel.service.HotelService;
import hotel.service.dto.input.BookingIdDTO;
import hotel.service.dto.input.RoomCreateDTO;
import hotel.service.dto.input.RoomNumberDTO;
import hotel.service.dto.output.BookingFullDTO;
import hotel.utility.Analytics;
import hotel.utility.Displayer;

public class HotelServiceAppl {

	public static void main(String[] args) throws Exception {
		
		
		
		try {
			HotelApplContext context = new HotelApplContext(new ConsoleInputOutput(), HotelService.getInstance(), new Analytics(HotelService.getInstance()), 
					RoomTypesPersistenceEmbeddedImp.getInstance(), RoomsPersistenceEmbeddedImp.getInstance(), 
					GuestsPersistenceEmbeddedImp.getInstance(), BookingsPersistenceEmbeddedImp.getInstance());
			boolean filesExist = Files.exists(Path.of(HotelApplConstants.DIR, HotelApplConstants.ROOMTYPE_FILE))
					&& Files.exists(Path.of(HotelApplConstants.DIR, HotelApplConstants.ROOM_FILE))
					&& Files.exists(Path.of(HotelApplConstants.DIR, HotelApplConstants.GUEST_FILE))
					&& Files.exists(Path.of(HotelApplConstants.DIR, HotelApplConstants.BOOKING_FILE));
			IHotelPersistenceService servicePersistence = HotelPersistenceService.getInstance(context);
			if (filesExist) {
				servicePersistence.loadHotelData();
				System.out.println("- Hotel data is loaded from file");
			} else {
				System.out.println("- Data files not found");
				RandomHotelDataGenerator randomHotelData = new RandomHotelDataGenerator(context);
				randomHotelData.generate();
				System.out.println("- Hotel data is generated");
			}
			System.out.println();
			Displayer.displayRoomTypes(context.getHotelService().getRoomTypes().values());
			Displayer.displayRooms(context.getHotelService().getRooms().values());
			Displayer.displayGuests(context.getHotelService().getGuests().values());
			Displayer.displayBookings(context.getHotelService().getBookings().values());
			Displayer.displayAnalitics(context.getHotelService());
			
			System.out.println("=".repeat(30));
			Booking firstBooking = context.getHotelService().getBookings().values().stream()
                    .findFirst().orElse(null);
			if (firstBooking != null) {
                System.out.println("Cancel first booking: " + context.getHotelService().removeBooking(new BookingIdDTO(firstBooking.getBookingId())));
                System.out.println("Bookings count after cancellation: " + context.getHotelService().getBookings().size());
            }
            else
            	System.out.println("Booking list is empty");
            System.out.println("=".repeat(30));
            Room removableRoom = context.getHotelService().getRooms().get(100);
            if (removableRoom == null) {
                RoomType roomType = context.getHotelService().getRoomTypes().values().stream().findFirst().orElse(null);
                if (roomType != null) {
                    Room extraRoom = new Room(new RoomCreateDTO(100, roomType.getTypeId()));
                    context.getHotelService().addRoom(extraRoom);
                    System.out.println("Added extra room 100 for removal demo");
                    System.out.println("Remove room 100: " + context.getHotelService().removeRoom(new RoomNumberDTO(100)));
                }
            }
            System.out.println("=".repeat(30));
            Guest firstGuest = context.getHotelService().getGuests()
            		.values().stream().findFirst().orElse(null);
            if(Objects.nonNull(firstGuest)) {
            	System.out.println("Bookings of first " + firstGuest + ":");
            	List<BookingFullDTO> bookingsOfFirstGuest = context.getHotelService().getBookingsByGuestsId(firstGuest.getId());
            	if(bookingsOfFirstGuest.isEmpty())
            		System.out.println("no suitable bookings are found");
            	else
            		bookingsOfFirstGuest.stream().forEach(System.out::println);
            }
            else
            	System.out.println("Guest list is empty");
            System.out.println("=".repeat(30));
            firstBooking = context.getHotelService().getBookings().values().stream()
                    .findFirst().orElse(null);
            if(Objects.nonNull(firstBooking)) {
            	List<Booking> bookingsOnDate= context.getHotelService().getBookingsStartOn(firstBooking.getCheckIn());
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
