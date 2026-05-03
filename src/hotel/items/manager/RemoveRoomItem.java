package hotel.items.manager;

import java.util.List;

import hotel.HotelApplContext;
import hotel.items.HotelItem;
import hotel.service.dto.input.RoomNumberDTO;
import hotel.service.dto.output.RoomDTO;

public class RemoveRoomItem extends HotelItem {

	public RemoveRoomItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "Remove room";
	}

	@Override
	public void perform() {
		try {
			List<RoomDTO> roomTypes = hotelService.getRoomsDTO();
			inOut.outputlLine("Rooms list:");
			showRooms(roomTypes, "no room types are found");
			RoomNumberDTO roomId = getExistingRoom();
			RoomDTO removed = hotelService.removeRoom(roomId);
			inOut.output("Room removed: room number - %d, category - %s, price per night - %.2f, capacity - %d"
					.formatted(removed.RoomNumber(), removed.category(), removed.pricePerNight(), removed.capacity()));
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}

	}

}
