package hotel.items.guest;

import java.util.List;

import hotel.HotelApplContext;
import hotel.items.HotelItem;
import hotel.service.dto.input.BookingDatesDTO;
import hotel.service.dto.output.RoomDTO;

public class ShowAvailableRoomsItem extends HotelItem{

	public ShowAvailableRoomsItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "Available rooms list";
	}

	@Override
	public void perform() {
		try {
			BookingDatesDTO dates = inputCheckInCheckOut();
			List<RoomDTO> availableRooms = hotelService.getAvailableRooms(dates);
			
			if(availableRooms.isEmpty()) {
				inOut.outputlLine("No available rooms for selected dates");
				return;
			}
			inOut.outputlLine("Available rooms for dates %s -> %s: ".formatted(dates.checkIn().toString(), dates.checkOut().toString()));
			showRooms(availableRooms, "No available rooms for selected dates");
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}
		
	}

}
