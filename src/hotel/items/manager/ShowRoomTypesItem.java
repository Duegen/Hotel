package hotel.items.manager;

import java.util.List;

import hotel.HotelApplContext;
import hotel.items.HotelItem;
import hotel.service.dto.output.RoomTypeDTO;

public class ShowRoomTypesItem extends HotelItem {

	public ShowRoomTypesItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "Show room types";
	}

	@Override
	public void perform() {
		inOut.outputlLine("Room types list:");
		List<RoomTypeDTO> roomTypes = hotelService.getRoomTypesDTO();
		showRoomTypes(roomTypes, "no room types are found");
	}

}
