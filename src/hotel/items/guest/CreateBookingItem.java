package hotel.items.guest;

import java.util.List;
import java.util.Objects;

import hotel.HotelApplContext;
import hotel.items.HotelItem;
import hotel.model.*;
import hotel.service.dto.input.BookingCreateDTO;
import hotel.service.dto.input.BookingDatesDTO;
import hotel.service.dto.input.GuestLoginDTO;
import hotel.service.dto.input.RoomNumberDTO;
import hotel.service.dto.output.BookingDTO;
import hotel.service.dto.output.RoomDTO;

public class CreateBookingItem extends HotelItem{

	protected CreateBookingItem(HotelApplContext context) {
		super(context);		
	}

	@Override
	public String displayName() {
		return "Create booking";
	}
	
	@Override
	public void perform() {
		try {
			GuestLoginDTO login = login();
			Guest guest = hotelService.login(login);
			inOut.outputlLine("Welcome guest '%s'".formatted(guest.getName()));
			
			BookingDatesDTO dates = inputCheckInCheckOut();
			List<RoomDTO> availableRooms = hotelService.getAvailableRooms(dates);
			if(availableRooms.isEmpty()) {
				inOut.outputlLine("No available rooms for selected dates");
				return;
			}
			inOut.outputlLine("Available rooms: ");
			showRooms(availableRooms, "No available rooms for selected dates");
			RoomNumberDTO roomNumber = getExistingRoom();
			if(Objects.isNull(roomNumber)) {
				inOut.outputlLine("Room number can't be null");
				return;
			}
				
			BookingCreateDTO bookingDTO = new BookingCreateDTO(guest.getId(), roomNumber.roomNumber(), dates.checkIn(), dates.checkOut());	
			BookingDTO booking = hotelService.createBooking(bookingDTO);
			inOut.outputlLine("Booking created: "+ booking);
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}
	}

}
