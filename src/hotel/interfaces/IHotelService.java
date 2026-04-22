package hotel.interfaces;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

public interface IHotelService {
	void addRoomType(String name, double pricePerNight, int capacity) throws RoomTypeAlreadyExistsException;
	void addRoom(int roomNumber, RoomType roomType) throws RoomAlreadyExistsException, RoomTypeNotFoundException;
	void addGuest(int guestId, String name, String email, String password) throws GuestAlreadyExistsException, DuplicateEmailException;
	void createBooking(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut) throws BookingAlreadyExistsException, GuestNotFoundException, RoomNotFoundException, NoFreeBookingIdException, RoomUnAvailableException;
	void addBooking(Booking booking) throws BookingAlreadyExistsException, GuestNotFoundException,
			RoomNotFoundException, NoFreeBookingIdException, RoomUnAvailableException;
	
	boolean removeRoomType(String name, double pricePerNight, int capacity) throws RemoveRoomTypeAssignException, RoomTypeNotFoundException;
	boolean removeRoom(int roomNumber) throws RoomNotFoundException, RemoveRoomAssignException;
	boolean removeGuest(int guestId) throws GuestNotFoundException, RemoveGuestAssignException;
	boolean cancelBooking(int bookingId) throws BookingNotFoundException, RemoveBookingActiveException;
	
	boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut);
	Optional<RoomType> findRoomType(String name, double pricePerNight, int capacity);
	Optional<Room> findRoomByNumber(int roomNumber);
	Optional<Guest> findGuestById(int guestId);
	Optional<Booking> findBookingById(int bookingId);
	
	List<RoomType> getRoomTypes();
	List<Room> getRooms();
	List<Guest> getGuests();
	List<Booking> getBookings();
}
