package hotel.interfaces;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import hotel.model.*;
import hotel.service.exceptions.*;

public interface IHotelService {
	RoomType createRoomType(String name, double pricePerNight, int capacity) throws NoFreeRoomTypeIdException, RoomTypeDuplicateException;
	void addRoomType(RoomType roomType) throws RoomTypeAlreadyExistsException, NoFreeRoomTypeIdException, RoomTypeDuplicateException;
	void addRoom(int roomNumber, RoomType roomType) throws RoomAlreadyExistsException, RoomTypeNotFoundException;
	Guest createGuest(String name, String email, LocalDate dateOfBirth, String password) throws DuplicateEmailException, NoFreeGuestIdException;
	void addGuest(Guest guest) throws DuplicateEmailException, NoFreeGuestIdException, GuestAlreadyExistsException;
	Booking createBooking(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut) throws BookingAlreadyExistsException, GuestNotFoundException, RoomNotFoundException, NoFreeBookingIdException, RoomUnAvailableException;
	void addBooking(Booking booking) throws BookingAlreadyExistsException, GuestNotFoundException,
			RoomNotFoundException, NoFreeBookingIdException, RoomUnAvailableException;
	void addBookingToDate(Booking booking);
	
	RoomType removeRoomType(int roomTypeId) throws RemoveRoomTypeAssignException, RoomTypeNotFoundException;
	Room removeRoom(int roomNumber) throws RoomNotFoundException, RemoveRoomAssignException;
	Guest removeGuest(int guestId) throws GuestNotFoundException, RemoveGuestAssignException;
	Booking cancelBooking(int bookingId) throws BookingNotFoundException, RemoveBookingActiveException;
	
	boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut);
	
	Map<Integer, RoomType> getRoomTypes();
	Map<Integer, Room> getRooms();
	Map<Integer, Guest> getGuests();
	Map<Integer, Booking> getBookings();
	Map<LocalDate, List<Booking>> getBookingsCheckInDate();
	
	List<Booking> getBookingsStartOn(LocalDate checkInDate);
	List<Booking> getBookingsByGuestsId(int guestId);
}
