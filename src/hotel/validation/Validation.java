package hotel.validation;

import java.time.LocalDate;
import java.util.Objects;

import hotel.model.Booking;
import hotel.model.Guest;
import hotel.model.Room;
import hotel.model.RoomType;

public class Validation {
	
	public static boolean validateRoomType(RoomType roomType){
		if(Objects.isNull(roomType))
			return false;
		if(roomType.getTypeId() < Constants.TYPE_MIN_ID || roomType.getTypeId() > Constants.TYPE_MAX_ID)
			return false;
		if(Objects.isNull(roomType.getName()) || roomType.getName().isBlank())
			return false;
		if(roomType.getCapacity() < Constants.ROOM_MIN_CAP || roomType.getCapacity() > Constants.ROOM_MAX_CAP)
			return false;
		if(roomType.getPricePerNight() < Constants.PRICE_PER_NIGHT_MIN || roomType.getPricePerNight() > Constants.PRICE_PER_NIGHT_MAX)
			return false;
		return true;
	}
	
	public static boolean validateRoom(Room room){
		if(Objects.isNull(room)) return false;
		if(Objects.isNull(room.getType())) return false;
		if(room.getRoomNumber() < Constants.ROOM_MIN_NUMBER || room.getRoomNumber() > Constants.ROOM_MAX_NUMBER)
			return false;
		if(!validateRoomType(room.getType()))
			return false;
		return true;
	}
	
	public static boolean validateGuest(Guest guest){
		if(Objects.isNull(guest))
			return false;
		if(guest.getId() < Constants.GUEST_MIN_ID || guest.getId() > Constants.GUEST_MAX_ID) 
			return false;
		if(Objects.isNull(guest.getName()) || guest.getName().isBlank())
			return false;
		if(Objects.isNull(guest.getEmail()) || !guest.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
			return false;
		if(Objects.isNull(guest.getDateOfBirth()) || guest.getDateOfBirth().isAfter(LocalDate.now()))
			return false;
		return true;
	}
	
	public static boolean validateBooking(Booking booking) {
		if(Objects.isNull(booking))
			return false;
		if(booking.getBookingId() < Constants.BOOKING_MIN_ID || booking.getBookingId() > Constants.BOOKING_MAX_ID) 
			return false;
		if(!validateGuest(booking.getGuest()))
			return false;
		if(!validateRoom(booking.getRoom()))
			return false;
		if(Objects.isNull(booking.getCheckIn()) || Objects.isNull(booking.getCheckOut()))
			return false;
		if(booking.getCheckIn().compareTo(booking.getCheckOut()) > 0)
			return false;
		return true;
	}

	public static boolean validateRoomNumber(int roomNumber) {
		return roomNumber >= Constants.ROOM_MIN_NUMBER && roomNumber <= Constants.ROOM_MAX_NUMBER;
	}
	
	public static boolean validateGuestId(int guestId) {
		return guestId >= Constants.GUEST_MIN_ID && guestId <= Constants.GUEST_MAX_ID;
	}

	public static boolean validateBookingId(int bookingId) {
		return bookingId >= Constants.BOOKING_MIN_ID && bookingId <= Constants.BOOKING_MAX_ID;
	}

	public static boolean validateRoomTypeId(int roomTypeId) {
		return roomTypeId >= Constants.TYPE_MIN_ID && roomTypeId <= Constants.TYPE_MAX_ID;
	}
}
