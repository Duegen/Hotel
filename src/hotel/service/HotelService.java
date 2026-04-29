package hotel.service;

import java.security.spec.KeySpec;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import hotel.HotelApplConstants;
import hotel.interfaces.IHotelService;
import hotel.model.*;
import hotel.service.exceptions.roomtype.*;
import hotel.service.exceptions.room.*;
import hotel.service.exceptions.guest.*;
import hotel.service.dto.input.*;
import hotel.service.dto.output.*;
import hotel.service.exceptions.*;
import hotel.service.exceptions.booking.*;
import hotel.validation.Constants;
import hotel.validation.Validation;
import hotel.validation.exceptions.ValidationException;
import hotel.validation.exceptions.roomtype.DTORoomTypeNullException;

public class HotelService implements IHotelService {
	private static HotelService instance;
	private final Map<Integer, RoomType> roomTypes;
	private final Map<Integer, Booking> bookings;
	private final Map<Integer, Room> rooms;
	private final Map<Integer, Guest> guests;
	private final Map<LocalDate, List<Booking>> bookingsCheckInDate;

	private HotelService() {
		roomTypes = new TreeMap<>();
		rooms = new TreeMap<>();
		guests = new TreeMap<>();
		bookings = new TreeMap<>();
		bookingsCheckInDate = new TreeMap<>();
	}

	public static HotelService getInstance() {
		if (instance == null)
			instance = new HotelService();
		return instance;
	}
	
	@Override
	public Map<Integer, RoomType> getRoomTypes() {
		return Collections.unmodifiableMap(roomTypes);
	}
	
	@Override
	public List<RoomTypeDTO> getRoomTypesDTO() {
		return roomTypes.values().stream()
				.map(rt -> new RoomTypeDTO(rt.getTypeId(), rt.getCategory(), rt.getPricePerNight(), rt.getCapacity())).toList();
	}
	
	@Override
	public Map<Integer, Booking> getBookings() {
		return Collections.unmodifiableMap(bookings);
	}
	
	@Override
	public List<BookingFullDTO> getBookingsDTO() {
		return bookings.values().stream()
				.map(bk -> {
					Guest guest = guests.get(bk.getGuestId());
					RoomType rt = roomTypes.get(rooms.get(bk.getRoomNumber()).getType());
					return new BookingFullDTO(bk.getBookingId(), guest, bk.getRoomNumber(), rt, bk.getCheckIn(), bk.getCheckOut());
				}).toList();
				
	}
	
	@Override
	public Map<Integer, Room> getRooms() {
		return Collections.unmodifiableMap(rooms);
	}
	
	@Override
	public List<RoomDTO> getRoomsDTO() {
		return rooms.values().stream()
				.map(r -> {
					RoomType rt = roomTypes.get(r.getType());
					return new RoomDTO(r.getRoomNumber(), rt.getCategory(), rt.getPricePerNight(), rt.getCapacity());
				}).toList();
	}
	
	@Override
	public Map<Integer, Guest> getGuests() {
		return Collections.unmodifiableMap(guests);
	}

	@Override
	public List<GuestDTO> getGuestsDTO() {
		return guests.values().stream()
				.map(g -> new GuestDTO(g.getId(), g.getName(), g.getEmail(), g.getDateOfBirth())).toList();
	}
	
	@Override
	public Map<LocalDate, List<Booking>> getBookingsCheckInDate() {
		Map<LocalDate, List<Booking>> result = new TreeMap<>();
		bookingsCheckInDate.forEach(
				(date, bookingList) -> result.put(date, Collections.unmodifiableList(new ArrayList<>(bookingList))));
		return Collections.unmodifiableMap(result);
	}
	
	@Override
	public RoomTypeDTO createRoomType(RoomTypeCreateDTO dto) throws RoomTypeException, ValidationException {
		if(Objects.isNull(dto))
			throw new DTORoomTypeNullException();
		int roomTypeId = findFreeId(roomTypes, Constants.TYPE_MIN_ID, Constants.TYPE_MAX_ID);
		if (roomTypeId == -1)
			throw new NoFreeRoomTypeIdException();
		RoomType roomType = new RoomType(roomTypeId, dto);
		Validation.validate(roomType);
		if (roomTypes.values().stream().anyMatch(rt -> rt.isDuplicate(roomType)))
			throw new RoomTypeDuplicateException(roomType);
		roomTypes.put(roomTypeId, roomType);
		return new RoomTypeDTO(roomType.getTypeId(), roomType.getCategory(), roomType.getPricePerNight(), roomType.getCapacity());
	}

	@Override
	public void addRoomType(RoomType roomType) throws RoomTypeException, ValidationException {
		Validation.validate(roomType);
		if (roomTypes.containsKey(roomType.getTypeId()))
			throw new RoomTypeAlreadyExistsException(roomType.getTypeId());
		if (roomTypes.values().stream().anyMatch(rt -> rt.isDuplicate(roomType)))
			throw new RoomTypeDuplicateException(roomType);
		roomTypes.put(roomType.getTypeId(), roomType.copy());
	}

	@Override
	public RoomTypeDTO removeRoomType(RoomTypeIdDTO dto) throws RoomTypeException, ValidationException {
		Validation.validate(dto);
		if (!roomTypes.containsKey(dto.roomTypeId()))
			throw new RoomTypeNotFoundException(dto.roomTypeId());
		RoomType roomType = roomTypes.get(dto.roomTypeId());
		boolean typeIsUsedByRooms = rooms.values().stream().map(Room::getType).anyMatch(type -> type.equals(roomType));
		if (typeIsUsedByRooms)
			throw new RemoveRoomTypeAssignException(dto.roomTypeId());
		RoomType removedRoomType = roomTypes.remove(dto.roomTypeId());
		return new RoomTypeDTO(removedRoomType.getTypeId(), removedRoomType.getCategory(), removedRoomType.getPricePerNight(), removedRoomType.getCapacity());
	}

	@Override
	public RoomDTO createRoom(RoomCreateDTO dto)
			throws RoomException, RoomTypeException, ValidationException {
		Room room = new Room(dto);
		Validation.validate(room);
		if (rooms.containsKey(dto.roomNumber()))
			throw new RoomAlreadyExistsException(room.getRoomNumber());
		if (!roomTypes.containsKey(dto.roomTypeId()))
			throw new RoomTypeNotFoundException(room.getType());
		rooms.put(dto.roomNumber(), room);
		RoomType roomType = roomTypes.get(room.getType());
		return new RoomDTO(room.getRoomNumber(), roomType.getCategory(), roomType.getPricePerNight(), roomType.getCapacity());
	}
	
	@Override
	public void addRoom(Room room)
			throws RoomException, RoomTypeException, ValidationException {
		Validation.validate(room);
		if (rooms.containsKey(room.getRoomNumber()))
			throw new RoomAlreadyExistsException(room.getRoomNumber());
		if (!roomTypes.containsKey(room.getType()))
			throw new RoomTypeNotFoundException(room.getType());
		rooms.put(room.getRoomNumber(), room.copy());
	}

	@Override
	public RoomDTO removeRoom(RoomNumberDTO dto) throws RoomException, ValidationException {
		Validation.validate(dto);
		if (!rooms.containsKey(dto.roomNumber()))
			throw new RoomNotFoundException(dto.roomNumber());
		boolean hasRelatedBookings = bookings.values().stream().anyMatch(booking -> booking.getRoomNumber() == dto.roomNumber());
		if (hasRelatedBookings)
			throw new RemoveRoomAssignException(dto.roomNumber());
		Room remove = rooms.remove(dto.roomNumber());
		RoomType rt = roomTypes.get(remove.getType());
		return new RoomDTO(remove.getRoomNumber(), rt.getCategory(), rt.getPricePerNight(), rt.getCapacity());
	}
	
	@Override
	public GuestCreatedDTO createGuest(GuestCreateDTO dto)
			throws GuestException, ValidationException {
		int guestId = findFreeId(guests, Constants.GUEST_MIN_ID, Constants.GUEST_MAX_ID);
		if (guestId == -1)
			throw new NoFreeGuestIdException();
		Validation.validatePassword(dto.password());
		Guest guest = new Guest(guestId, dto, HotelApplConstants.ALGORITHM, HotelApplConstants.KEY_LENGTH);
		Validation.validate(guest);
		if (guests.values().stream().anyMatch(g -> g.getEmail().equals(dto.email())))
			throw new DuplicateEmailException(dto.email());
		guests.put(guestId, guest);
		return new GuestCreatedDTO(guest.getName(), guest.getEmail(), guest.getDateOfBirth());
	}

	@Override
	public void addGuest(Guest guest)
			throws GuestException, ValidationException {
		Validation.validate(guest);
		if (guests.containsKey(guest.getId()))
			throw new GuestAlreadyExistsException(guest.getId());
		if (guests.values().stream().anyMatch(g -> g.getEmail().equals(guest.getEmail())))
			throw new DuplicateEmailException(guest.getEmail());
		guests.put(guest.getId(), guest.copy());
	}

	@Override
	public GuestDTO removeGuest(GuestIdDTO dto) throws GuestException, ValidationException {
		Validation.validate(dto);
		if (!guests.containsKey(dto.guestId()))
			throw new GuestNotFoundException(dto.guestId());
		boolean hasRelatedBookings = bookings.values().stream().anyMatch(booking -> booking.getGuestId() == dto.guestId());
		if (hasRelatedBookings)
			throw new RemoveGuestAssignException(dto.guestId());
		Guest removed = guests.remove(dto.guestId());
		return new GuestDTO(removed.getId(), removed.getName(), removed.getEmail(), removed.getDateOfBirth());
	}
	
	@Override
	public void addBooking(Booking booking) throws BookingException, GuestException,
			RoomException, ValidationException {
		if (Validation.validate(booking)) {
			if (!guests.containsKey(booking.getGuestId()))
				throw new GuestNotFoundException(booking.getBookingId());
			if (!rooms.containsKey(booking.getRoomNumber()))
				throw new RoomNotFoundException(booking.getRoomNumber());
			if (!isRoomAvailable(booking.getRoomNumber(), booking.getCheckIn(), booking.getCheckOut()))
				throw new RoomUnAvailableException(booking.getRoomNumber(), booking.getCheckIn(),
						booking.getCheckOut());
			if (bookings.containsKey(booking.getBookingId()))
				throw new BookingAlreadyExistsException(booking.getBookingId());
			bookings.put(booking.getBookingId(), booking.copy());
			addBookingToDate(bookings.get(booking.getBookingId()));
		} else
			throw new IllegalArgumentException("invalid booking data");
	}

	@Override
	public BookingDTO createBooking(BookingCreateDTO dto)
			throws BookingException, GuestException, RoomException, ValidationException {
		int bookingId = findFreeId(bookings, Constants.BOOKING_MIN_ID, Constants.BOOKING_MAX_ID);
		if (bookingId == -1)
			throw new NoFreeBookingIdException();
		Booking booking = new Booking(bookingId, dto);
		Validation.validate(booking); 
		if (!guests.containsKey(dto.guestId()))
			throw new GuestNotFoundException(dto.guestId());
		if (!rooms.containsKey(dto.roomNumber()))
			throw new RoomNotFoundException(dto.roomNumber());
		if (!isRoomAvailable(dto.roomNumber(), dto.checkIn(), dto.checkOut()))
			throw new RoomUnAvailableException(dto.guestId(), dto.checkIn(), dto.checkOut());
		bookings.put(bookingId, booking);
		addBookingToDate(booking);
		RoomType roomType = roomTypes.get(rooms.get(booking.getRoomNumber()).getType());
		return new BookingDTO(bookingId, booking.getRoomNumber(), roomType.getCategory(), roomType.getPricePerNight(), 
				roomType.getCapacity(), booking.getCheckIn(), booking.getCheckOut());
	}

	@Override
	public BookingDTO cancelBooking(BookingIdDTO dto) throws BookingException, ValidationException {
		Validation.validate(dto);
		if (!bookings.containsKey(dto.bookingId()))
			throw new BookingNotFoundException(dto.bookingId());
		Booking booking = bookings.get(dto.bookingId());
		boolean isBookingActive = LocalDate.now().isAfter(booking.getCheckIn())
				&& LocalDate.now().isBefore(booking.getCheckOut());
		if (isBookingActive)
			throw new RemoveBookingActiveException(booking.getBookingId());
		if(booking.getCheckOut().isBefore(LocalDate.now()))
			throw new RemoveExpireBookingException(booking.getBookingId());
		removeBookingFromDate(booking);
		booking = bookings.remove(dto.bookingId());
		RoomType roomType = roomTypes.get(rooms.get(booking.getRoomNumber()).getType());
		
		return new BookingDTO(booking.getBookingId(), booking.getRoomNumber(),roomType.getCategory(), 
				roomType.getPricePerNight(), roomType.getCapacity(),booking.getCheckIn(), booking.getCheckOut());
	}
	
	public BookingFullDTO removeBooking(BookingIdDTO dto) throws BookingException, ValidationException {
		Validation.validate(dto);
		if (!bookings.containsKey(dto.bookingId()))
			throw new BookingNotFoundException(dto.bookingId());
		Booking booking = bookings.get(dto.bookingId());
		boolean isBookingActive = LocalDate.now().isAfter(booking.getCheckIn())
				&& LocalDate.now().isBefore(booking.getCheckOut());
		if (isBookingActive)
			throw new RemoveBookingActiveException(booking.getBookingId());
		removeBookingFromDate(booking);
		booking = bookings.remove(dto.bookingId());
		RoomType roomType = roomTypes.get(rooms.get(booking.getRoomNumber()).getType());
		Guest guest = guests.get(booking.getGuestId());
		return new BookingFullDTO(booking.getBookingId(), guest, booking.getRoomNumber(), roomType,booking.getCheckIn(), booking.getCheckOut());
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
	public boolean isRoomAvailable(int roomNumber, LocalDate checkIn, LocalDate checkOut) {
		if (Objects.isNull(checkIn) || Objects.isNull(checkOut))
			throw new IllegalArgumentException("arg can not be null");
		if (!checkOut.isAfter(checkIn)) {
			throw new IllegalArgumentException("check-out must be after check-in");
		}
		return bookings.values().stream().filter(booking -> booking.getRoomNumber() == roomNumber)
				.noneMatch(booking -> booking.overlaps(checkIn, checkOut));
	}

	@SuppressWarnings("unused")
	@Override
	public void addBookingToDate(Booking booking) {
		bookingsCheckInDate.computeIfAbsent(booking.getCheckIn(), k -> new ArrayList<>()).add(booking);
	}

	@Override
	public boolean removeBookingFromDate(Booking booking) {
		List<Booking> list = bookingsCheckInDate.get(booking.getCheckIn());
		if (list == null)
			return false;
		list.removeIf(e -> e.getBookingId() == booking.getBookingId());
		if (list.isEmpty())
			bookingsCheckInDate.remove(booking.getCheckIn());
		return true;
	}

	@Override
	public List<Booking> getBookingsStartOn(LocalDate checkInDate) {
		return bookings.values().stream().filter(bk -> bk.getCheckIn().equals(checkInDate)).toList();
	}

	@Override
	public List<BookingDTO> getBookingsForGuest(int guestId) {
		List<Booking> guestBookings = bookings.values().stream().filter(bk -> bk.getBookingId() == guestId).toList();
		return guestBookings.stream().map(bk -> {
			RoomType roomType = roomTypes.get(rooms.get(bk.getRoomNumber()).getType());
			return new BookingDTO(bk.getBookingId(), bk.getRoomNumber(), roomType.getCategory(), roomType.getPricePerNight(), roomType.getCapacity(), bk.getCheckIn(), bk.getCheckOut());
		}).toList();
	}
	
	@Override
	public List<BookingFullDTO> getBookingsByGuestsId(int guestId) {
		List<Booking> guestBookings = bookings.values().stream().filter(bk -> bk.getBookingId() == guestId).toList();
		return guestBookings.stream().map(bk -> {
			Guest guest = guests.get(bk.getGuestId());
			RoomType roomType = roomTypes.get(rooms.get(bk.getRoomNumber()).getType());
			return new BookingFullDTO(bk.getBookingId(), guest, bk.getRoomNumber(), roomType, bk.getCheckIn(), bk.getCheckOut());
		}).toList();
	}

	@Override
	public Room findRoomByNumber(int roomNumber) {
		return rooms.get(roomNumber);
	}


	@Override
	public RoomType findRoomTypeById(int roomTypeId) {
		return roomTypes.get(roomTypeId);
	}


	@Override
	public Guest findGuestById(int guestId) {
		return guests.get(guestId);
	}
	
	@Override
	public Guest findGuestByEmail(String email) {
		if(Objects.isNull(email))
			return null;
		return guests.values().stream().filter(g -> email.equals(g.getEmail())).findFirst().orElse(null);
	}

	@Override
	public Booking findBookingById(int bookingId) {
		return bookings.get(bookingId);
	}


	@Override
	public void rebuildBookingsByCheckInDate() {
		bookingsCheckInDate.clear();
		bookings.values().forEach(this::addBookingToDateIndex);
	}
	
	@SuppressWarnings("unused")
	private void addBookingToDateIndex(Booking booking) {
		bookingsCheckInDate.computeIfAbsent(booking.getCheckIn(), key -> new ArrayList<>()).add(booking);
	}
	
	@Override
	public List<RoomDTO> getAvailableRooms(BookingDatesDTO dto) throws ValidationException {
		Validation.validate(dto);
		
		return rooms.values().stream()
				.filter(room -> isRoomAvailable(room.getRoomNumber(), dto.checkIn(), dto.checkOut()))
				.map(room -> {
					RoomType roomType = roomTypes.get(room.getType()); 
					return new RoomDTO(room.getRoomNumber(), roomType.getCategory(), roomType.getPricePerNight(), roomType.getCapacity());	
				})
				.toList();
	}
	

	@Override
	public Guest login(GuestLoginDTO dto) throws ValidationException, GuestException {
		Validation.validate(dto);
		Guest guest = findGuestByEmail(dto.email());
		if(Objects.isNull(guest))
			throw new GuestLoginException();
		String hashedPassword;
		try {
			String[] parts = guest.getPassword().split(":");
			byte[] salt = Base64.getDecoder().decode(parts[0]);
			KeySpec spec = new PBEKeySpec(dto.password().toCharArray(), salt, 65536, HotelApplConstants.KEY_LENGTH);
			SecretKeyFactory factory;
			factory = SecretKeyFactory.getInstance(HotelApplConstants.ALGORITHM);
			hashedPassword = factory.generateSecret(spec).getEncoded().toString();
		} catch (Exception e) {
			throw new GuestLoginErrorException();
		}
        if(!guest.getPassword().equals(hashedPassword))
			throw new GuestLoginException();
        return guest;
	}


}
