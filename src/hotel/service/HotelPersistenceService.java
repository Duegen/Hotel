package hotel.service;

import java.io.IOException;
import java.util.Collection;
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
			saveRoomType(serviceInstance.getRoomTypes().values());
			saveRooms(serviceInstance.getRooms().values());
			saveGuests(serviceInstance.getGuests().values());
			saveBookings(serviceInstance.getBookings().values());
	}

	public boolean loadHotelData() throws IOException{
		List<RoomType> roomTypesLoaded = loadRoomTypes();
		List<Room> roomsLoaded = loadRooms();
		List<Guest> guestsLoaded = loadGuests();
		List<Booking> bookingsLoaded = loadBookings();
		
		roomTypesLoaded.stream().forEach(rt -> {
			try {
				serviceInstance.addRoomType(rt);
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
				serviceInstance.addGuest(g);
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

	private void saveRoomType(Collection<RoomType> roomTypes) throws IOException {
		if (Objects.isNull(roomTypes))
			throw new IllegalArgumentException("List of room types is null");
		List<RoomType> validated = roomTypes.stream().filter(roomType 
				-> Validation.validateRoomType(roomType))
				.toList();
		roomTypesPersistenceImp.saveRoomTypes(validated);
	}

	private List<RoomType> loadRoomTypes() throws IOException {
		List<RoomType> roomTypes = roomTypesPersistenceImp.loadRoomTypes();
		return roomTypes.stream().filter(roomType -> Validation.validateRoomType(roomType))
			.toList();
	}

	private void saveRooms(Collection<Room> rooms) throws IOException {
		if (Objects.isNull(rooms))
			throw new IllegalArgumentException("List of rooms is null");
		List<Room> validated = rooms.stream().filter(room 
				-> Validation.validateRoom(room)).toList();
		roomsPersistenceImp.saveRooms(validated);
	}

	private List<Room> loadRooms() throws IOException {
		List<Room> rooms = roomsPersistenceImp.loadRooms();
		return rooms.stream().filter(room -> Validation.validateRoom(room)).toList();
	}
	
	private void saveGuests(Collection<Guest> guests) throws IOException {
		if (Objects.isNull(guests))
			throw new IllegalArgumentException("List of guests is null");
		List<Guest> validated = guests.stream().filter(guest 
				-> Validation.validateGuest(guest))
				.toList();
		guestsPersistenceImp.saveGuests(validated);
	}

	private List<Guest> loadGuests() throws IOException {
		List<Guest> guests = guestsPersistenceImp.loadGuests();
		return guests.stream().filter(guest 
				-> Validation.validateGuest(guest)).toList();
	}
	
	private void saveBookings(Collection<Booking> bookings) throws IOException {
		if (Objects.isNull(bookings))
			throw new IllegalArgumentException("List of bookings is null");
		List<Booking> validated = bookings.stream().filter(booking 
				-> Validation.validateBooking(booking)).toList();
		bookingsPersistenceImp.saveBookings(validated);
	}

	private List<Booking> loadBookings() throws IOException {
		List<Booking> bookings = bookingsPersistenceImp.loadBookings();
		return bookings.stream().filter(booking -> Validation.validateBooking(booking))
				.toList();
	}
	
}
