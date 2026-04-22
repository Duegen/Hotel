package hotel.validation;

import java.time.LocalDate;
import java.util.Objects;

import hotel.model.Guest;
import hotel.model.Room;
import hotel.model.RoomType;

public class Validation {
	
	public static boolean validateRoomType(String name, double pricePerNight, int capacity){
		if(Objects.isNull(name) || name.isBlank())
			return false;
		if(capacity < Constants.ROOM_MIN_CAP || capacity > Constants.ROOM_MAX_CAP)
			return false;
		if(pricePerNight < Constants.PRICE_PER_NIGHT_MIN || pricePerNight > Constants.PRICE_PER_NIGHT_MAX)
			return false;
		return true;
	}
	
	public static boolean validateRoom(int roomNumber, RoomType roomType){
		if(Objects.isNull(roomType)) return false;
		if(roomNumber < Constants.ROOM_MIN_NUMBER || roomNumber > Constants.ROOM_MAX_NUMBER)
			return false;
		if(!validateRoomType(roomType.getName(), roomType.getPricePerNight(), roomType.getCapacity()))
			return false;
		return true;
	}
	
	public static boolean validateGuest(int guestId, String name, String email){
		if(guestId < Constants.GUEST_MIN_ID || guestId > Constants.GUEST_MAX_ID)
			return false;
		if(Objects.isNull(name) || name.isBlank())
			return false;
		if(Objects.isNull(email) || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
			return false;
		return true;
	}
	
	public static boolean validateBooking(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut) {
		if(!validateGuest(guest.getId(), guest.getName(), guest.getEmail()))
			return false;
		if(!validateRoom(room.getRoomNumber(), room.getType()))
			return false;
		if(Objects.isNull(checkIn) || Objects.isNull(checkOut))
			return false;
		if(checkIn.compareTo(checkOut) > 0)
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
}
