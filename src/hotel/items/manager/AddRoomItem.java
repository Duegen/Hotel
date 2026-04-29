package hotel.items.manager;

import java.util.List;

import hotel.HotelApplContext;
import hotel.items.HotelItem;
import hotel.service.dto.input.RoomCreateDTO;
import hotel.service.dto.output.RoomDTO;
import hotel.service.dto.output.RoomTypeDTO;

public class AddRoomItem extends HotelItem {

	protected AddRoomItem(HotelApplContext context) {
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
			inOut.outputlLine("Room created: "+ createdRoom);
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}
		
	}

}
