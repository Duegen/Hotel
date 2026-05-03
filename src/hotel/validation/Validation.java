package hotel.validation;

import java.util.Objects;

import hotel.model.Booking;
import hotel.model.Guest;
import hotel.model.Room;
import hotel.model.RoomType;
import hotel.service.dto.input.BookingDatesDTO;
import hotel.service.dto.input.BookingIdDTO;
import hotel.service.dto.input.GuestIdDTO;
import hotel.service.dto.input.GuestLoginDTO;
import hotel.service.dto.input.RoomNumberDTO;
import hotel.service.dto.input.RoomTypeIdDTO;
import hotel.validation.exceptions.ValidationException;
import hotel.validation.exceptions.booking.*;
import hotel.validation.exceptions.guest.*;
import hotel.validation.exceptions.room.*;
import hotel.validation.exceptions.roomtype.*;

public class Validation {
	
	public static boolean validate(RoomType roomType) throws ValidationException {
		if(Objects.isNull(roomType))
			throw new DTORoomTypeNullException();
		if(roomType.getTypeId() < Constants.TYPE_MIN_ID || roomType.getTypeId() > Constants.TYPE_MAX_ID)
			throw new DTORoomTypeIdException(roomType.getTypeId(), Constants.TYPE_MIN_ID, Constants.TYPE_MAX_ID);
		if(Objects.isNull(roomType.getCategory()) || roomType.getCategory().isBlank())
			throw new DTORoomTypeNameException(roomType.getCategory());
		if(roomType.getCapacity() < Constants.ROOM_MIN_CAP || roomType.getCapacity() > Constants.ROOM_MAX_CAP)
			throw new DTORoomTypeCapacityException(roomType.getCapacity(), Constants.ROOM_MIN_CAP, Constants.ROOM_MAX_CAP);
		if(roomType.getPricePerNight() < Constants.PRICE_PER_NIGHT_MIN || roomType.getPricePerNight() > Constants.PRICE_PER_NIGHT_MAX)
			throw new DTORoomTypePricePerNightException(roomType.getPricePerNight(), Constants.PRICE_PER_NIGHT_MIN, Constants.PRICE_PER_NIGHT_MAX);
		return true;
	}
	
	public static boolean validate(RoomTypeIdDTO dto) throws ValidationException {
		if(Objects.isNull(dto))
			throw new DTORoomTypeNullException();
		if(Objects.isNull(dto.roomTypeId()))
			throw new DTORoomTypeNullException();
		if (dto.roomTypeId() < Constants.TYPE_MIN_ID || dto.roomTypeId() > Constants.TYPE_MAX_ID)
			throw new DTORoomTypeIdException(dto.roomTypeId(), Constants.TYPE_MIN_ID, Constants.TYPE_MAX_ID);
		return true;
	}
	
	public static boolean validate(Room room) throws ValidationException{
		if(Objects.isNull(room)) 
			throw new DTORoomNullException();
		if(Objects.isNull(room.getType())) 
			throw new DTORoomTypeNullException();
		if(room.getRoomNumber() < Constants.ROOM_MIN_NUMBER || room.getRoomNumber() > Constants.ROOM_MAX_NUMBER)
			throw new DTORoomNumberException(room.getRoomNumber(), Constants.ROOM_MIN_NUMBER, Constants.ROOM_MAX_NUMBER);
		validate(new RoomTypeIdDTO(room.getType()));
		return true;
	}
	
	public static boolean validate(RoomNumberDTO dto) throws ValidationException {
		if(Objects.isNull(dto))
			throw new DTORoomNullException();
		if(Objects.isNull(dto.roomNumber()))
			throw new DTORoomNullException();
		if(dto.roomNumber() < Constants.ROOM_MIN_NUMBER || dto.roomNumber() > Constants.ROOM_MAX_NUMBER)
			throw new DTORoomNumberException(dto.roomNumber(), Constants.ROOM_MIN_NUMBER, Constants.ROOM_MAX_NUMBER);
		return true;
	}
	
	public static boolean validate(Guest guest) throws ValidationException {
		if(Objects.isNull(guest))
			throw new DTOGuestNullException();
		if(guest.getId() < Constants.GUEST_MIN_ID || guest.getId() > Constants.GUEST_MAX_ID) 
			throw new DTOGuestIdException(guest.getId(), Constants.GUEST_MIN_ID, Constants.GUEST_MAX_ID);
		if(Objects.isNull(guest.getName()) || guest.getName().isBlank())
			throw new DTOGuestNameException(guest.getName());
		if(Objects.isNull(guest.getEmail()) || !guest.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
			throw new DTOGuestEmailFormatException(guest.getEmail());
		if(Objects.isNull(guest.getDateOfBirth()) || guest.getAge() < 18)
			throw new DTOGuestDateOfBirthException(guest.getDateOfBirth());
		if(Objects.isNull(guest.getPassword()))
				throw new DTOGuestPasswordNullException();
		return true;
	}
	
	public static boolean validate(GuestIdDTO dto) throws ValidationException {
		if(Objects.isNull(dto))
			throw new DTOGuestNullException();
		if(Objects.isNull(dto.guestId()))
			throw new DTOGuestNullException();
		if(dto.guestId() < Constants.GUEST_MIN_ID || dto.guestId() > Constants.GUEST_MAX_ID)
			throw new DTOGuestIdException(dto.guestId(), Constants.GUEST_MIN_ID, Constants.GUEST_MAX_ID);
		return true;
	}
	
	public static boolean validate(Booking booking) throws ValidationException {
		if(Objects.isNull(booking))
			throw new DTOBookingNullException();
		if(booking.getBookingId() < Constants.BOOKING_MIN_ID || booking.getBookingId() > Constants.BOOKING_MAX_ID) 
			throw new DTOBookingIdException(booking.getBookingId(), Constants.BOOKING_MIN_ID, Constants.BOOKING_MAX_ID);
		validate(new GuestIdDTO(booking.getGuestId()));
		validate(new RoomNumberDTO(booking.getRoomNumber()));
		if(Objects.isNull(booking.getCheckIn()))
			throw new DTOBookingCheckInException();
		if(Objects.isNull(booking.getCheckOut()))
			throw new DTOBookingCheckOutException();
		if(booking.getCheckIn().compareTo(booking.getCheckOut()) > 0)
			throw new DTOBookingDateCollisionException(booking.getCheckIn(), booking.getCheckOut());
		return true;
	}

	public static boolean validate(BookingIdDTO dto) throws ValidationException {
		if(Objects.isNull(dto))
			throw new DTOBookingNullException();
		if(Objects.isNull(dto.bookingId()))
			throw new DTOBookingNullException();
		if(dto.bookingId() < Constants.BOOKING_MIN_ID || dto.bookingId() > Constants.BOOKING_MAX_ID)
			throw new DTOBookingIdException(dto.bookingId(), Constants.BOOKING_MIN_ID, Constants.BOOKING_MAX_ID);
		return true;
	}

	public static boolean validatePassword(String password) throws ValidationException {
		if(Objects.isNull(password))
			throw new DTOGuestPasswordNullException();
		if(!password.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}"))
			throw new DTOGuestPasswordFormatException();
		return true;
	}

	public static void validate(GuestLoginDTO dto) throws ValidationException {
		if(Objects.isNull(dto))
			throw new DTOLoginNullException();
		if(Objects.isNull(dto.email()) || Objects.isNull(dto.password()))
			throw new DTOLoginNullException();
		if(!dto.email().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
			throw new DTOGuestEmailFormatException(dto.email());
		validatePassword(dto.password());	
	}

	public static void validate(BookingDatesDTO dto) throws ValidationException {
		if(Objects.isNull(dto))
			throw new DTOBookingNullException();
		if(Objects.isNull(dto.checkIn()) || Objects.isNull(dto.checkOut()))
			throw new DTOBookingDatesNullException();
		if (!dto.checkOut().isAfter(dto.checkIn()))
			throw new DTOBookingDateCollisionException(dto.checkIn(), dto.checkOut());
		
	}

	
}
