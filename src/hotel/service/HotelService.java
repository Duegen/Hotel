package hotel.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import hotel.interfaces.IHotelService;
import hotel.model.*;
import hotel.service.exceptions.*;
import hotel.validation.Constants;
import hotel.validation.Validation;

public class HotelService implements IHotelService {
	private static HotelService instance;
	private final Map<Integer, RoomType> roomTypes;
	private final Map<Integer, Booking> bookings;
	private final Map<Integer, Room> rooms;
	private final Map<Integer, Guest> guests;
	private final Map<LocalDate, List<Booking>> bookingsCheckInDate;

	private HotelService() {
		roomTypes = new HashMap<>();
		bookings = new HashMap<>();
		rooms = new HashMap<>();
		guests = new HashMap<>();
		bookingsCheckInDate = new TreeMap<>();
	}

	public static HotelService getInstance() {
		if (instance == null)
			instance = new HotelService();
		return instance;
	}

	public Map<Integer, RoomType> getRoomTypes() {
		return roomTypes;
	}

	public Map<Integer, Booking> getBookings() {
		return bookings;
	}

	public Map<Integer, Room> getRooms() {
		return rooms;
	}

	public Map<Integer, Guest> getGuests() {
		return guests;
	}

	@Override
	public RoomType createRoomType(String name, double pricePerNight, int capacity)
			throws NoFreeRoomTypeIdException, RoomTypeDuplicateException {
		int roomTypeId = findFreeId(roomTypes, Constants.TYPE_MIN_ID, Constants.TYPE_MAX_ID);
		if (roomTypeId == -1)
			throw new NoFreeRoomTypeIdException();
		RoomType roomType = new RoomType(roomTypeId, name, pricePerNight, capacity);
		if (Validation.validateRoomType(roomType)) {
			if (roomTypes.values().stream().anyMatch(rt -> rt.isSame(roomType)))
				throw new RoomTypeDuplicateException(roomType);
			roomTypes.put(roomTypeId, roomType);
			return roomType;
		} else
			throw new IllegalArgumentException("invalid room type data");
	}
	
	@Override
	public void addRoomType(RoomType roomType)
			throws RoomTypeAlreadyExistsException, NoFreeRoomTypeIdException, RoomTypeDuplicateException {
		if (Validation.validateRoomType(roomType)) {
			if(roomTypes.containsKey(roomType.getTypeId()))
				throw new RoomTypeAlreadyExistsException(roomType.getTypeId());
			if (roomTypes.values().stream().anyMatch(rt -> rt.isSame(roomType)))
				throw new RoomTypeDuplicateException(roomType);
			roomTypes.put(roomType.getTypeId(), roomType);
		} else
			throw new IllegalArgumentException("invalid room type data");
	}

	@Override
	public Room addRoom(int roomNumber, RoomType roomType)
			throws RoomAlreadyExistsException, RoomTypeNotFoundException {
		Room room = new Room(roomNumber, roomType);
		if (Validation.validateRoom(room)) {
			if (rooms.containsKey(roomNumber))
				throw new RoomAlreadyExistsException(room.getRoomNumber());
			if (!roomTypes.containsKey(roomType.getTypeId()))
				throw new RoomTypeNotFoundException(room.getType().getTypeId());
			rooms.put(roomNumber, room);
			return room;
		} else
			throw new IllegalArgumentException("invalid room data");
	}

	@Override
	public Guest createGuest(String name, String email, LocalDate dateOfBirth, String password)
			throws DuplicateEmailException, NoFreeGuestIdException {
		int guestId = findFreeId(guests, Constants.GUEST_MIN_ID, Constants.GUEST_MAX_ID);
		if (guestId == -1)
			throw new NoFreeGuestIdException();
		Guest guest = new Guest(guestId, name, email, dateOfBirth, password);
		if (Validation.validateGuest(guest)) {
			if (guests.values().stream().anyMatch(g -> g.getEmail().equals(email)))
				throw new DuplicateEmailException(email);
			guests.put(guestId, guest);
			return guest;
		} else
			throw new IllegalArgumentException("invalid guest data");
	}
	
	@Override
	public void addGuest(Guest guest)
			throws DuplicateEmailException, NoFreeGuestIdException, GuestAlreadyExistsException {
		if (Validation.validateGuest(guest)) {
			if(guests.containsKey(guest.getId()))
				throw new GuestAlreadyExistsException(guest.getId());
			if (guests.values().stream().anyMatch(g -> g.getEmail().equals(guest.getEmail())))
				throw new DuplicateEmailException(guest.getEmail());
			guests.put(guest.getId(), guest);
		} else
			throw new IllegalArgumentException("invalid guest data");
	}

	@Override
	public void addBooking(Booking booking) throws BookingAlreadyExistsException, GuestNotFoundException,
			RoomNotFoundException, NoFreeBookingIdException, RoomUnAvailableException {
		if (Validation.validateBooking(booking)) {
			if (!guests.containsKey(booking.getGuest().getId()))
				throw new GuestNotFoundException(booking.getGuest().getId());
			if (!rooms.containsKey(booking.getRoom().getRoomNumber()))
				throw new RoomNotFoundException(booking.getRoom().getRoomNumber());
			if (!isRoomAvailable(booking.getRoom(), booking.getCheckIn(), booking.getCheckOut()))
				throw new RoomUnAvailableException(booking.getRoom().getRoomNumber(), booking.getCheckIn(),
						booking.getCheckOut());
			if (bookings.containsKey(booking.getBookingId()))
				throw new BookingAlreadyExistsException(booking.getBookingId());
			bookings.put(booking.getBookingId(), booking);
		} else
			throw new IllegalArgumentException("invalid booking data");
	}

	@Override
	public Booking createBooking(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut)
			throws BookingAlreadyExistsException, GuestNotFoundException, RoomNotFoundException,
			NoFreeBookingIdException, RoomUnAvailableException {
		int bookingId = findFreeId(bookings, Constants.BOOKING_MIN_ID, Constants.BOOKING_MIN_ID);
		if (bookingId == -1)
			throw new NoFreeBookingIdException();
		Booking booking = new Booking(bookingId, guest, room, checkIn, checkOut);
		if (Validation.validateBooking(booking)) {
			if (!guests.containsKey(guest.getId()))
				throw new GuestNotFoundException(guest.getId());
			if (!rooms.containsKey(room.getRoomNumber()))
				throw new RoomNotFoundException(room.getRoomNumber());
			if (!isRoomAvailable(room, checkIn, checkOut))
				throw new RoomUnAvailableException(room.getRoomNumber(), checkIn, checkOut);
			bookings.put(bookingId, booking);
			return booking;
		} else
			throw new IllegalArgumentException("invalid booking data");
	}

	private <R> int findFreeId(Map<Integer, R> sourceMap, int minId, int maxId) {
		int freeId = minId;
		for (int id : sourceMap.keySet()) {
			if (id != freeId)
				break;
			freeId++;
		}
		return freeId > maxId ? -1 : freeId;
	}

	@Override
	public RoomType removeRoomType(int roomTypeId) throws RemoveRoomTypeAssignException, RoomTypeNotFoundException {
		if (!Validation.validateRoomTypeId(roomTypeId))
			throw new IllegalArgumentException("invalid room type id");
		if(!roomTypes.containsKey(roomTypeId))
			throw new RoomTypeNotFoundException(roomTypeId);
		RoomType roomType = roomTypes.get(roomTypeId);
		boolean typeIsUsedByRooms = rooms.values().stream()
	            .map(Room::getType)
	            .anyMatch(type -> type.equals(roomType));
		if (typeIsUsedByRooms) 
			throw new RemoveRoomTypeAssignException(roomTypeId);
		return roomTypes.remove(roomTypeId);	
	}

	@Override
	public Room removeRoom(int roomNumber) throws RoomNotFoundException, RemoveRoomAssignException {
		if (!Validation.validateRoomNumber(roomNumber))
			throw new IllegalArgumentException("invalid room number");
		if(!rooms.containsKey(roomNumber))
			throw new RoomNotFoundException(roomNumber);
		Room room = rooms.get(roomNumber);
		boolean hasRelatedBookings = bookings.values().stream()
				.anyMatch(booking -> booking.getRoom().equals(room));
		if (hasRelatedBookings)
			throw new RemoveRoomAssignException(roomNumber);
		return rooms.remove(roomNumber);

	}

	@Override
	public Guest removeGuest(int guestId) throws GuestNotFoundException, RemoveGuestAssignException {
		if (!Validation.validateGuestId(guestId))
			throw new IllegalArgumentException("invalid guest id");
		if (!guests.containsKey(guestId))
			throw new GuestNotFoundException(guestId);
		Guest guest = guests.get(guestId);
		boolean hasRelatedBookings = bookings.values().stream()
				.anyMatch(booking -> booking.getGuest().equals(guest));
		if (hasRelatedBookings)
			throw new RemoveGuestAssignException(guestId);
		return guests.remove(guestId);
	}

	@Override
	public Booking cancelBooking(int bookingId) throws BookingNotFoundException, RemoveBookingActiveException {
		if (!Validation.validateBookingId(bookingId))
			throw new IllegalArgumentException("invalid booking id");
		if (!bookings.containsKey(bookingId))
			throw new BookingNotFoundException(bookingId);
		Booking booking = bookings.get(bookingId);
		boolean isBookingActive = LocalDate.now().isAfter(booking.getCheckIn())
				&& LocalDate.now().isBefore(booking.getCheckOut());
		if (isBookingActive)
			throw new RemoveBookingActiveException(booking);
		return bookings.remove(bookingId);
	}

	@Override
	public boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
		if (Objects.isNull(room) || Objects.isNull(checkIn) || Objects.isNull(checkOut))
			throw new IllegalArgumentException("arg can not be null");
		if (!checkOut.isAfter(checkIn)) {
			throw new IllegalArgumentException("check-out must be after check-in");
		}
		return bookings.values().stream()
				.filter(booking -> booking.getRoom().equals(room))
				.noneMatch(booking -> booking.overlaps(checkIn, checkOut));
	}

	@SuppressWarnings("unused")
	@Override
	public void addBookingToDate(Booking booking) {
		bookingsCheckInDate.computeIfAbsent(booking.getCheckIn(), k -> new ArrayList<>()).add(booking);
	}
	
	@Override
	public boolean removeBookingFromCheckIn(Booking booking) {
		List<Booking> list = bookingsCheckInDate.get(booking.getCheckIn());
		if(list == null)
			return false;
		list.removeIf(e->e.getBookingId()==booking.getBookingId());
		if(list.isEmpty())
			bookingsCheckInDate.remove(booking.getCheckIn());
		return true;
	}
	
	@Override
	public Map<LocalDate, List<Booking>> getBookingsCheckInDate() {
		return bookingsCheckInDate;
	}

	@Override
	public List<Booking> getBookingsStartOn(LocalDate checkInDate) {
		return bookings.values().stream().filter(bk -> bk.getCheckIn().equals(checkInDate)).toList();
	}

	@Override
	public List<Booking> getBookingsByGuestsId(int guestId) {
		return bookings.values().stream().filter(bk -> bk.getGuest().getId() == guestId).toList();
	}

}
