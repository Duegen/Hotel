package hotel.items.manager;

import java.util.List;

import hotel.HotelApplContext;
import hotel.items.HotelItem;
import hotel.service.dto.input.RoomCreateDTO;
import hotel.service.dto.output.RoomDTO;
import hotel.service.dto.output.RoomTypeDTO;

public class AddRoomItem extends HotelItem {

	public AddRoomItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "Add new room";
	}

	@Override
	public void perform() {
		try {
			inOut.outputlLine("List of room types:");
			List<RoomTypeDTO> roomTypes = hotelService.getRoomTypesDTO();
			showRoomTypes(roomTypes, "No available room types are found");
			RoomCreateDTO newRoom = addRoom();
			RoomDTO createdRoom = hotelService.createRoom(newRoom);
			inOut.outputlLine("Room created: room number - %d, category - %s, price per night - %.2f, capacity - %d"
					.formatted(createdRoom.RoomNumber(), createdRoom.category(), createdRoom.pricePerNight(), createdRoom.capacity()));
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}
		
	}

}
