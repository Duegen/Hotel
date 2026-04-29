package hotel.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import hotel.HotelApplConstants;
import hotel.HotelApplContext;
import hotel.interfaces.IHotelPersistenceService;
import hotel.interfaces.IHotelService;
import hotel.model.*;
import hotel.validation.Validation;
import hotel.validation.exceptions.ValidationException;

public class HotelPersistenceService implements IHotelPersistenceService {
	private final HotelApplContext context;
	private static HotelPersistenceService instance;

	private HotelPersistenceService(HotelApplContext context) {
		this.context = context;
	}

	public static HotelPersistenceService getInstance(HotelApplContext context) {
		if (instance == null)
			instance = new HotelPersistenceService(context);
		return instance;
	}

	public void saveHotelData() throws IOException {
		IHotelService service = context.getHotelService();
		saveRoomTypes(service.getRoomTypes().values(), Paths.get(HotelApplConstants.DIR, HotelApplConstants.ROOMTYPE_FILE));
		saveRooms(service.getRooms().values(), Paths.get(HotelApplConstants.DIR, HotelApplConstants.ROOM_FILE));
		saveGuests(service.getGuests().values(), Paths.get(HotelApplConstants.DIR, HotelApplConstants.GUEST_FILE));
		saveBookings(service.getBookings().values(), Paths.get(HotelApplConstants.DIR, HotelApplConstants.BOOKING_FILE));
	}

	public boolean loadHotelData() throws IOException {
		IHotelService service = context.getHotelService();
		List<RoomType> roomTypesLoaded = loadRoomTypes(Paths.get(HotelApplConstants.DIR, HotelApplConstants.ROOMTYPE_FILE));
		List<Room> roomsLoaded = loadRooms(Paths.get(HotelApplConstants.DIR, HotelApplConstants.ROOM_FILE));
		List<Guest> guestsLoaded = loadGuests(Paths.get(HotelApplConstants.DIR, HotelApplConstants.GUEST_FILE));
		List<Booking> bookingsLoaded = loadBookings(Paths.get(HotelApplConstants.DIR, HotelApplConstants.BOOKING_FILE));

		roomTypesLoaded.stream().forEach(rt -> {
			try {
				service.addRoomType(rt);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		});
		roomsLoaded.stream().forEach(r -> {
			try {
				service.addRoom(r);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		});
		guestsLoaded.stream().forEach(g -> {
			try {
				service.addGuest(g);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		});
		bookingsLoaded.stream().forEach(bk -> {
			try {
				service.addBooking(bk);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		});
		return true;
	}

	private void saveRoomTypes(Collection<RoomType> roomTypes, Path dataFile) throws IOException {
		if (Objects.isNull(roomTypes))
			throw new IllegalArgumentException("List of room types is null");
		List<RoomType> validated = roomTypes.stream().filter(roomType -> {
			try {
				return Validation.validate(roomType);
			} catch (ValidationException e) {
				System.out.println(e.getMessage());
				return false;
			}
		}).toList();
		context.getRoomTypePersistence().saveRoomTypes(validated, dataFile);
	}

	private List<RoomType> loadRoomTypes(Path dataFile) throws IOException {
		List<RoomType> roomTypes = context.getRoomTypePersistence().loadRoomTypes(dataFile);
		return roomTypes.stream().filter(roomType -> {
			try {
				return Validation.validate(roomType);
			} catch (ValidationException e) {
				System.out.println(e.getMessage());
				return false;
			}
		}).toList();
	}

	private void saveRooms(Collection<Room> rooms, Path dataFile) throws IOException {
		if (Objects.isNull(rooms))
			throw new IllegalArgumentException("List of rooms is null");
		List<Room> validated = rooms.stream().filter(room -> {
			try {
				return Validation.validate(room);
			} catch (ValidationException e) {
				System.out.println(e.getMessage());
				return false;
			}
		}).toList();
		context.getRoomPersistence().saveRooms(validated, dataFile);
	}

	private List<Room> loadRooms(Path dataFile) throws IOException {
		List<Room> rooms = context.getRoomPersistence().loadRooms(dataFile);
		return rooms.stream().filter(room -> {
			try {
				return Validation.validate(room);
			} catch (ValidationException e) {
				System.out.println(e.getMessage());
				return false;
			}
		}).toList();
	}

	private void saveGuests(Collection<Guest> guests, Path dataFile) throws IOException {
		if (Objects.isNull(guests))
			throw new IllegalArgumentException("List of guests is null");
		List<Guest> validated = guests.stream().filter(guest -> {
			try {
				return Validation.validate(guest);
			} catch (ValidationException e) {
				System.out.println(e.getMessage());
				return false;
			}
		}).toList();
		context.getGuestPersistence().saveGuests(validated, dataFile);
	}

	private List<Guest> loadGuests(Path dataFile) throws IOException {
		List<Guest> guests = context.getGuestPersistence().loadGuests(dataFile);
		return guests.stream().filter(guest -> {
			try {
				return Validation.validate(guest);
			} catch (ValidationException e) {
				System.out.println(e.getMessage());
				return false;
			}
		}).toList();
	}

	private void saveBookings(Collection<Booking> bookings, Path dataFile) throws IOException {
		if (Objects.isNull(bookings))
			throw new IllegalArgumentException("List of bookings is null");
		List<Booking> validated = bookings.stream().filter(booking -> {
			try {
				return Validation.validate(booking);
			} catch (ValidationException e) {
				System.out.println(e.getMessage());
				return false;
			}
		}).toList();
		context.getBookingPersistence().saveBookings(validated, dataFile);
	}

	private List<Booking> loadBookings(Path dataFile) throws IOException {
		List<Booking> bookings = context.getBookingPersistence().loadBookings(dataFile);
		return bookings.stream().filter(booking -> {
			try {
				return Validation.validate(booking);
			} catch (ValidationException e) {
				System.out.println(e.getMessage());
				return false;
			}
		}).toList();
	}

}
