package hotel.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import hotel.generator.Constants;
import hotel.interfaces.IHotelService;
import hotel.model.*;
import hotel.service.exceptions.BookingAlreadyExistsException;
import hotel.service.exceptions.BookingNotFoundException;
import hotel.service.exceptions.DuplicateEmailException;
import hotel.service.exceptions.GuestAlreadyExistsException;
import hotel.service.exceptions.GuestNotFoundException;
import hotel.service.exceptions.NoFreeBookingIdException;
import hotel.service.exceptions.RemoveBookingActiveException;
import hotel.service.exceptions.RemoveGuestAssignException;
import hotel.service.exceptions.RemoveRoomAssignException;
import hotel.service.exceptions.RemoveRoomTypeAssignException;
import hotel.service.exceptions.RoomAlreadyExistsException;
import hotel.service.exceptions.RoomNotFoundException;
import hotel.service.exceptions.RoomTypeAlreadyExistsException;
import hotel.service.exceptions.RoomTypeNotFoundException;
import hotel.service.exceptions.RoomUnAvailableException;
import hotel.validation.Validation;

public class HotelService implements IHotelService{
	private static HotelService instance;
	private final List<RoomType> roomTypes;
	private final List<Booking> bookings;
	private final List<Room> rooms;
	private final List<Guest> guests;

	private HotelService() {
		roomTypes = new ArrayList<RoomType>();
		bookings = new ArrayList<Booking>();
		rooms = new ArrayList<Room>();
		guests = new ArrayList<Guest>();
	}

	public static HotelService getInstance() {
		if (instance == null)
			instance = new HotelService();
		return instance;
	}

	public List<RoomType> getRoomTypes() {
		return roomTypes;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public List<Room> getRooms() {
		return rooms;
	}
	
	public List<Guest> getGuests() {
		return guests;
	}

	@Override
	public void addRoomType(String name, double pricePerNight, int capacity) throws RoomTypeAlreadyExistsException {
		RoomType roomType = new RoomType(name, pricePerNight, capacity);
		if(Validation.validateRoomType(name, pricePerNight, capacity)) {
			if(roomTypes.contains(roomType))
				throw new RoomTypeAlreadyExistsException(roomType);
			roomTypes.add(roomType);
		}else
			throw new IllegalArgumentException("invalid room type data");
	}

	@Override
	public void addRoom(int roomNumber, RoomType roomType) throws RoomAlreadyExistsException, RoomTypeNotFoundException {
		Room room = new Room(roomNumber, roomType);
		if(Validation.validateRoom(roomNumber, roomType)) {
			if(rooms.contains(room))
				throw new RoomAlreadyExistsException(room.getRoomNumber());
			if(!roomTypes.contains(roomType))
				throw new RoomTypeNotFoundException(room.getType());
			rooms.add(room);
		}else
			throw new IllegalArgumentException("invalid room data");	
	}

	@Override
	public void addGuest(int guestId, String name, String email, String password) throws GuestAlreadyExistsException, DuplicateEmailException {
		Guest guest = new Guest(guestId, name, email, password);
		if(Validation.validateGuest(guestId, name, email)) {
			if(guests.contains(guest))
				throw new GuestAlreadyExistsException(guest.getId());
			if(guests.stream().anyMatch(g -> g.getEmail().equals(email)))
				throw new DuplicateEmailException(email);
			guests.add(guest);
		}else
			throw new IllegalArgumentException("invalid guest data");			
	}
	
	@Override
	public void addBooking(Booking booking) throws BookingAlreadyExistsException, GuestNotFoundException, RoomNotFoundException, NoFreeBookingIdException, RoomUnAvailableException {
		if(Validation.validateBooking(booking.getGuest(), booking.getRoom(), booking.getCheckIn(), booking.getCheckOut())) {
			if(!guests.contains(booking.getGuest()))
				throw new GuestNotFoundException(booking.getGuest().getId());
			if(!rooms.contains(booking.getRoom()))
				throw new RoomNotFoundException(booking.getRoom().getRoomNumber());
			if (!isRoomAvailable(booking.getRoom(), booking.getCheckIn(), booking.getCheckOut())) 
	            throw new RoomUnAvailableException(booking.getRoom().getRoomNumber(), booking.getCheckIn(), booking.getCheckOut());
			bookings.add(booking);
		}else
			throw new IllegalArgumentException("invalid booking data");
	}
	
	@Override
	public void createBooking(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut) throws BookingAlreadyExistsException, GuestNotFoundException, RoomNotFoundException, NoFreeBookingIdException, RoomUnAvailableException {
		int bookingId = findFreeBookingId();
		if(bookingId == -1)
			throw new NoFreeBookingIdException();
		if(Validation.validateBooking(guest, room, checkIn, checkOut)) {
			if(!guests.contains(guest))
				throw new GuestNotFoundException(guest.getId());
			if(!rooms.contains(room))
				throw new RoomNotFoundException(room.getRoomNumber());
			if (!isRoomAvailable(room, checkIn, checkOut)) 
	            throw new RoomUnAvailableException(room.getRoomNumber(), checkIn, checkOut);
			Booking booking = new Booking(bookingId, guest, room, checkIn, checkOut);
			bookings.add(booking);
		}else
			throw new IllegalArgumentException("invalid booking data");
	}

	private int findFreeBookingId() {
		int id = Constants.BOOKING_MIN_ID;
		for (Booking booking : bookings) {
			if(booking.getBookingId() != id)
				break;
			id++;
		}
		return id > Constants.BOOKING_MAX_ID ? -1 : id;
		
	}

	@Override
	public boolean removeRoomType(String name, double pricePerNight, int capacity) throws RemoveRoomTypeAssignException, RoomTypeNotFoundException {
		if(Validation.validateRoomType(name, pricePerNight, capacity)) {
			Optional<RoomType> roomType = findRoomType(name, pricePerNight, capacity);
			if(roomType.isEmpty())
				throw new RoomTypeNotFoundException(new RoomType(name, pricePerNight, capacity));
			boolean typeIsUsedByRooms = rooms.stream()
	                .map(Room::getType)
	                .anyMatch(type -> type.equals(roomType.get()));
			if (typeIsUsedByRooms) 
	            throw new RemoveRoomTypeAssignException(roomType.get());
			return roomTypes.remove(roomType.get());
		}	
		else
			throw new IllegalArgumentException("invalid room type data");
	}

	@Override
	public boolean removeRoom(int roomNumber) throws RoomNotFoundException, RemoveRoomAssignException {
		if(!Validation.validateRoomNumber(roomNumber))
        	throw new IllegalArgumentException("invalid room number");
		Optional<Room> room = findRoomByNumber(roomNumber);
		if(room.isEmpty())
			throw new RoomNotFoundException(roomNumber);
		boolean hasRelatedBookings = bookings.stream()
                .anyMatch(booking -> booking.getRoom().equals(room));
        if (hasRelatedBookings) 
        	throw new RemoveRoomAssignException(roomNumber);
        return rooms.remove(room.get());

	}

	@Override
	public boolean removeGuest(int guestId) throws GuestNotFoundException, RemoveGuestAssignException {
		if(!Validation.validateGuestId(guestId))
        	throw new IllegalArgumentException("invalid guest id");
		Optional<Guest> guest = findGuestById(guestId);
		if(guest.isEmpty())
			throw new GuestNotFoundException(guestId);
		boolean hasRelatedBookings = bookings.stream()
                .anyMatch(booking -> booking.getGuest().equals(guest.get()));
        if (hasRelatedBookings) 
        	throw new RemoveGuestAssignException(guestId);
        return guests.remove(guest.get());
	}

	@Override
	public boolean cancelBooking(int bookingId) throws BookingNotFoundException, RemoveBookingActiveException {
		if(!Validation.validateBookingId(bookingId))
        	throw new IllegalArgumentException("invalid booking id");
		Optional<Booking> booking = findBookingById(bookingId);
		if(booking.isEmpty())
			throw new BookingNotFoundException(bookingId);
		boolean isBookingActive = LocalDate.now().isAfter(booking.get().getCheckIn()) 
				&& LocalDate.now().isBefore(booking.get().getCheckOut());
        if (isBookingActive) 
        	throw new RemoveBookingActiveException(booking.get());
        return bookings.remove(booking.get());
	}

	@Override
    public boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        if(Objects.isNull(room) || Objects.isNull(checkIn) || Objects.isNull(checkOut))
        	throw new IllegalArgumentException("arg can not be null");
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out must be after check-in");
        }
        return bookings.stream()
                .filter(booking -> booking.getRoom().equals(room))
                .noneMatch(booking -> booking.overlaps(checkIn, checkOut));
    }

    @Override
    public Optional<Room> findRoomByNumber(int roomNumber) {
    	return rooms.stream()
                .filter(room -> room.getRoomNumber() == roomNumber)
                .findFirst();
    }


    @Override
    public Optional<RoomType> findRoomType(String name, double pricePerNight, int capacity) {
        if (Objects.isNull(name)) 
            return Optional.empty();
        RoomType roomType = new RoomType(name, pricePerNight, capacity);
        return roomTypes.stream()
                .filter(type -> type.equals(roomType))
                .findFirst();
    }


    @Override
    public Optional<Guest> findGuestById(int guestId) {
        return guests.stream()
                .filter(guest -> guest.getId() == guestId)
                .findFirst();
    }


    @Override
    public Optional<Booking> findBookingById(int bookingId) {
        return bookings.stream()
                .filter(booking -> booking.getBookingId() == bookingId)
                .findFirst();
    }
}
