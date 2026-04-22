package hotel.service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import hotel.interfaces.IBookingsPersistence;
import hotel.interfaces.IGuestsPersistence;
import hotel.interfaces.IHotelPersistenceService;
import hotel.interfaces.IHotelService;
import hotel.interfaces.IRoomTypesPersistence;
import hotel.interfaces.IRoomsPersistence;
import hotel.model.*;
import hotel.persistence.BookingsPersistenceEmbeddedImp;
import hotel.persistence.GuestPersistenceEmbeddedImp;
import hotel.persistence.RoomTypesPersistenceEmbeddedImp;
import hotel.persistence.RoomsPersistenceEmbeddedImp;
import hotel.validation.Validation;

public class HotelPersistenceService implements IHotelPersistenceService{
	private final IHotelService serviceInstance;
	private static HotelPersistenceService instance;
	private IBookingsPersistence bookingsPersistenceImp = BookingsPersistenceEmbeddedImp.getInstance();
	private IRoomTypesPersistence roomTypesPersistenceImp = RoomTypesPersistenceEmbeddedImp.getInstance();
	private IRoomsPersistence roomsPersistenceImp = RoomsPersistenceEmbeddedImp.getInstance();
	private IGuestsPersistence guestsPersistenceImp = GuestPersistenceEmbeddedImp.getInstance();
	
	private HotelPersistenceService(IHotelService service) {
		if(Objects.isNull(service)) throw new IllegalArgumentException("service is null");
		this.serviceInstance = service;
	}

	public static HotelPersistenceService getInstance(IHotelService service) {
		if (instance == null)
			instance = new HotelPersistenceService(service);
		return instance;
	}
	
	public void saveHotelData() throws IOException {
			saveRoomType(serviceInstance.getRoomTypes());
			saveRooms(serviceInstance.getRooms());
			saveGuests(serviceInstance.getGuests());
			saveBookings(serviceInstance.getBookings());
	}

	public boolean loadHotelData() throws IOException{
		List<RoomType> roomTypesLoaded = loadRoomTypes();
		List<Room> roomsLoaded = loadRooms();
		List<Guest> guestsLoaded = loadGuests();
		List<Booking> bookingsLoaded = loadBookings();
		
		roomTypesLoaded.stream().forEach(rt -> {
			try {
				serviceInstance.addRoomType(rt.getName(), rt.getPricePerNight(), rt.getCapacity());
			} catch (Exception e) {
				
			}
		});
		roomsLoaded.stream().forEach(r -> {
			try {
				serviceInstance.addRoom(r.getRoomNumber(), r.getType());
			} catch (Exception e) {
				
			}
		});
		guestsLoaded.stream().forEach(g -> {
			try {
				serviceInstance.addGuest(g.getId(), g.getName(), g.getEmail(), null);
			} catch (Exception e) {
				
			}
		});
		bookingsLoaded.stream().forEach(bk -> {
			try {
				serviceInstance.addBooking(bk);
			} catch (Exception e) {
				
			}
		});
		return true;
	}

	private void saveRoomType(List<RoomType> roomTypes) throws IOException {
		if (Objects.isNull(roomTypes))
			throw new IllegalArgumentException("List of room types is null");
//		if (roomTypes.size() == 0)
//			throw new IllegalArgumentException("List of room types is empty");
		List<RoomType> validated = roomTypes.stream().filter(roomType 
				-> Validation.validateRoomType(roomType.getName(), roomType.getPricePerNight(), roomType.getCapacity()))
				.toList();
//		if (validated.size() == 0)
//			throw new IllegalArgumentException("No valid data in room types list");
		roomTypesPersistenceImp.saveRoomTypes(validated);
	}

	private List<RoomType> loadRoomTypes() throws IOException {
		List<RoomType> roomTypes = roomTypesPersistenceImp.loadRoomTypes();
		return roomTypes.stream().filter(roomType 
				-> Validation.validateRoomType(roomType.getName(), roomType.getPricePerNight(), roomType.getCapacity()))
			.toList();
	}

	private void saveRooms(List<Room> rooms) throws IOException {
		if (Objects.isNull(rooms))
			throw new IllegalArgumentException("List of rooms is null");
//		if (rooms.size() == 0)
//			throw new IllegalArgumentException("List of rooms is empty");
		List<Room> validated = rooms.stream().filter(room 
				-> Validation.validateRoom(room.getRoomNumber(), room.getType())).toList();
//		if (validated.size() == 0)
//			throw new IllegalArgumentException("No valid data in rooms list");
		roomsPersistenceImp.saveRooms(validated);
	}

	private List<Room> loadRooms() throws IOException {
		List<Room> rooms = roomsPersistenceImp.loadRooms();
		return rooms.stream().filter(room -> Validation.validateRoom(room.getRoomNumber(), room.getType())).toList();
	}
	
	private void saveGuests(List<Guest> guests) throws IOException {
		if (Objects.isNull(guests))
			throw new IllegalArgumentException("List of guests is null");
		if (guests.size() == 0)
			throw new IllegalArgumentException("List of guests is empty");
		List<Guest> validated = guests.stream().filter(guest 
				-> Validation.validateGuest(guest.getId(), guest.getName(), guest.getEmail()))
				.toList();
//		if (validated.size() == 0)
//			throw new IllegalArgumentException("No valid data in guests list");
		guestsPersistenceImp.saveGuests(validated);
	}

	private List<Guest> loadGuests() throws IOException {
		List<Guest> guests = guestsPersistenceImp.loadGuests();
		return guests.stream().filter(guest 
				-> Validation.validateGuest(guest.getId(), guest.getName(), guest.getEmail())).toList();
	}
	
	private void saveBookings(List<Booking> bookings) throws IOException {
		if (Objects.isNull(bookings))
			throw new IllegalArgumentException("List of bookings is null");
		if (bookings.size() == 0)
			throw new IllegalArgumentException("List of bookings is empty");
		List<Booking> validated = bookings.stream().filter(booking 
				-> Validation.validateBooking(booking.getGuest(), booking.getRoom(), booking.getCheckIn(), booking.getCheckOut())).toList();
//		if (validated.size() == 0)
//			throw new IllegalArgumentException("No valid data in bookings list");
		bookingsPersistenceImp.saveBookings(validated);
	}

	private List<Booking> loadBookings() throws IOException {
		List<Booking> bookings = bookingsPersistenceImp.loadBookings();
		return bookings.stream().filter(booking -> Validation.validateBooking(booking.getGuest(), booking.getRoom(), booking.getCheckIn(), booking.getCheckOut()))
				.toList();
	}
	
}
