package hotel.items;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import cli.Inputoutput;
import cli.Item;
import hotel.HotelApplConstants;
import hotel.HotelApplContext;
import hotel.interfaces.*;
import hotel.service.dto.input.*;
import hotel.service.dto.output.*;

public abstract class HotelItem implements Item {
	protected final HotelApplContext context;
	protected final Inputoutput inOut;
	protected final IHotelService hotelService;
	protected final IAnalytics analyticsService;

	protected HotelItem(HotelApplContext context) {
		super();
		this.context = context;
		this.inOut = context.getInOut();
		this.hotelService = context.getHotelService();
		this.analyticsService = context.getAnalyticsService();
	}

	protected GuestLoginDTO login() {
		String email = inOut.inputString("Enter email");
		if(Objects.isNull(email))
			return null;
		String password = inOut.inputString("Enter password");
		if(Objects.isNull(password))
			return null;
		return new GuestLoginDTO(email, password);
	}
	
	protected GuestIdDTO getExistingGuest() {
		Integer guestId = inOut.inputInteger("Enter guest id");
		if (guestId == null)
			return null;
		return new GuestIdDTO(guestId);
	}

	protected RoomNumberDTO getExistingRoom() {
		Integer roomNumber = inOut.inputInteger("Enter room number");
		if (roomNumber == null)
			return null;
		return new RoomNumberDTO(roomNumber);
	}

	protected RoomTypeIdDTO getExistingRoomType() {
		Integer roomTypeId = inOut.inputInteger("Enter room type id");
		if (roomTypeId == null)
			return null;
		return new RoomTypeIdDTO(roomTypeId);
	}
	
	protected BookingIdDTO getExistingBooking() {
		Integer bookingId = inOut.inputInteger("Enter booking id");
		if (bookingId == null)
			return null;
		return new BookingIdDTO(bookingId);
	}

	protected BookingDatesDTO inputCheckInCheckOut() {
		LocalDate checkIn = inOut.inputDate("Enter check in date in format", HotelApplConstants.DATE_FORMAT);
		if (checkIn == null)
			return null;
		LocalDate checkOut = inOut.inputDate("Enter check out date in format", HotelApplConstants.DATE_FORMAT);
		if (checkOut == null)
			return null;
		if (!checkOut.isAfter(checkIn)) {
			inOut.outputlLine("Check out must be after check in");
			return null;
		}
		return new BookingDatesDTO(checkIn, checkOut);
	}
	
	protected RoomTypeCreateDTO addRoomType() {
		String name = inOut.inputString("Enter room type name");
		if(Objects.isNull(name))
			return null;
		Double pricePerNight = inOut.inputDouble("Enter price per night");
		if(Objects.isNull(pricePerNight))
			return null;
		Integer capacity = inOut.inputInteger("Enter room capacity");
		if(Objects.isNull(capacity))
			return null;
		return new RoomTypeCreateDTO(name, pricePerNight, capacity);
	}
	
	protected RoomTypeIdDTO removeRoomType() {
		Integer roomTypeId = inOut.inputInteger("Enter room type id");
		if(Objects.isNull(roomTypeId))
			return null;
		return new RoomTypeIdDTO(roomTypeId);
	}
	
	protected RoomCreateDTO addRoom() {
		Integer roomNumber = inOut.inputInteger("Enter room number");
		if(Objects.isNull(roomNumber))
			return null;
		Integer roomTypeId = inOut.inputInteger("Enter room type id");
		if(Objects.isNull(roomTypeId))
			return null;
		return new RoomCreateDTO(roomNumber, roomTypeId);
	}
	
	protected RoomTypeIdDTO removeRoom() {
		Integer roomId = inOut.inputInteger("Enter room id");
		if(Objects.isNull(roomId))
			return null;
		return new RoomTypeIdDTO(roomId);
	}
	
	protected GuestCreateDTO inputNewGuest() {
		String name = inOut.inputString("Enter guest name");
		if(Objects.isNull(name))
			return null;
		String email = inOut.inputString("Enter guest email");
		if(Objects.isNull(email))
			return null;
		String password = inOut.inputString("Enter guest password");
		if(Objects.isNull(password))
			return null;
		LocalDate birthDate = inOut.inputDate("Enter birth date in format", HotelApplConstants.DATE_FORMAT);
		if(Objects.isNull(birthDate))
			return null;
		return new GuestCreateDTO(name, email, birthDate, password);
	}
	
	protected GuestIdDTO removeGuest() {
		Integer guestId = inOut.inputInteger("Enter guest id");
		if(Objects.isNull(guestId))
			return null;
		return new GuestIdDTO(guestId);
	}

	protected void showBookings(List<BookingDTO> bookings, String newMessage) {
		if (bookings == null || bookings.isEmpty()) {
			inOut.outputlLine(newMessage);
			return;
		}
		bookings.stream().sorted((bk1, bk2) -> bk1.BookingId() - bk2.BookingId())
		.forEach(inOut::outputlLine);
	}
	
	protected void showFullBookings(List<BookingFullDTO> bookings, String newMessage) {
		if (bookings == null || bookings.isEmpty()) {
			inOut.outputlLine(newMessage);
			return;
		}
		bookings.stream().sorted((bk1, bk2) -> bk1.BookingId() - bk2.BookingId())
		.forEach(inOut::outputlLine);
	}

	protected void showRooms(List<RoomDTO> rooms, String newMessage) {
		if (rooms == null || rooms.isEmpty()) {
			inOut.outputlLine(newMessage);
			return;
		}
		rooms.stream().sorted((r1, r2) -> r1.RoomNumber() - r2.RoomNumber())
		.forEach(inOut::outputlLine);
	}
	
	protected void showRoomTypes(List<RoomTypeDTO> roomTypes, String newMessage) {
		if (roomTypes == null || roomTypes.isEmpty()) {
			inOut.outputlLine(newMessage);
			return;
		}
		roomTypes.stream().sorted((rt1, rt2) -> rt1.roomTypeId() - rt2.roomTypeId())
		.forEach(inOut::outputlLine);
	}
	
	protected void showGuests(List<GuestDTO> guests, String newMessage) {
		if (guests == null || guests.isEmpty()) {
			inOut.outputlLine(newMessage);
			return;
		}
		guests.stream().sorted((g1, g2) -> g1.guestId() - g2.guestId())
		.forEach(inOut::outputlLine);
	}
}
