package hotel.interfaces;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import hotel.model.*;
import hotel.service.dto.input.BookingCreateDTO;
import hotel.service.dto.input.BookingDatesDTO;
import hotel.service.dto.input.BookingIdDTO;
import hotel.service.dto.input.GuestCreateDTO;
import hotel.service.dto.input.GuestIdDTO;
import hotel.service.dto.input.GuestLoginDTO;
import hotel.service.dto.input.RoomCreateDTO;
import hotel.service.dto.input.RoomNumberDTO;
import hotel.service.dto.input.RoomTypeCreateDTO;
import hotel.service.dto.input.RoomTypeIdDTO;
import hotel.service.dto.output.BookingDTO;
import hotel.service.dto.output.BookingFullDTO;
import hotel.service.dto.output.GuestDTO;
import hotel.service.dto.output.RoomDTO;
import hotel.service.dto.output.RoomTypeDTO;
import hotel.service.exceptions.*;
import hotel.validation.exceptions.ValidationException;

public interface IHotelService {
	RoomTypeDTO createRoomType(RoomTypeCreateDTO dto) throws RoomTypeException, ValidationException;
	void addRoomType(RoomType roomType) throws RoomTypeException, ValidationException;
	RoomDTO createRoom(RoomCreateDTO dto) throws RoomException, RoomTypeException, ValidationException;
	void addRoom(Room dto) throws RoomException, RoomTypeException, ValidationException;
	Guest createGuest(GuestCreateDTO dto) throws GuestException, ValidationException;
	void addGuest(Guest dto) throws GuestException, ValidationException;
	BookingDTO createBooking(BookingCreateDTO dto) throws BookingException, GuestException, RoomException, ValidationException;
	void addBooking(Booking booking) throws BookingException, GuestException, RoomException, ValidationException;
	void addBookingToDate(Booking booking);
	
	RoomTypeDTO removeRoomType(RoomTypeIdDTO dto) throws RoomTypeException, ValidationException;
	RoomDTO removeRoom(RoomNumberDTO dto) throws RoomException, ValidationException;
	GuestDTO removeGuest(GuestIdDTO dto) throws GuestException, ValidationException;
	BookingDTO cancelBooking(BookingIdDTO dto) throws BookingException, ValidationException;
	BookingFullDTO removeBooking(BookingIdDTO dto) throws BookingException, ValidationException;
	boolean removeBookingFromDate(Booking booking);
	
	boolean isRoomAvailable(int roomNumber, LocalDate checkIn, LocalDate checkOut);
	
	Map<Integer, RoomType> getRoomTypes();
	Map<Integer, Room> getRooms();
	Map<Integer, Guest> getGuests();
	Map<Integer, Booking> getBookings();
	Map<LocalDate, List<Booking>> getBookingsCheckInDate();
	
	List<Booking> getBookingsStartOn(LocalDate checkInDate);
	List<BookingFullDTO> getBookingsByGuestsId(int guestId);
	
	Room findRoomByNumber(int roomNumber);
	RoomType findRoomTypeById(int roomTypeId);
	Guest findGuestById(int guestId);
	Booking findBookingById(int bookingId);
	
	void rebuildBookingsByCheckInDate();
	Entry<Integer, Integer> login(GuestLoginDTO login) throws ValidationException, GuestException;
	Guest findGuestByEmail(String email);
	
	List<BookingDTO> getBookingsForGuest(int guestId);
	List<RoomDTO> getAvailableRooms(BookingDatesDTO dto) throws ValidationException;
	List<RoomTypeDTO> getRoomTypesDTO();
	List<GuestDTO> getGuestsDTO();
	List<RoomDTO> getRoomsDTO();
	List<BookingFullDTO> getBookingsDTO();
}
