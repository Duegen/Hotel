package hotel.items.manager;

import java.util.List;

import hotel.HotelApplContext;
import hotel.items.HotelItem;
import hotel.service.dto.input.RoomTypeIdDTO;
import hotel.service.dto.output.RoomTypeDTO;

public class RemoveRoomTypeItem extends HotelItem {

	public RemoveRoomTypeItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "Remove room type";
	}

	@Override
	public void perform() {
		try {
			List<RoomTypeDTO> roomTypes = hotelService.getRoomTypesDTO();
			inOut.outputlLine("Room types list:");
			showRoomTypes(roomTypes, "no room types are found");
			RoomTypeIdDTO roomTypeId = getExistingRoomType();
			RoomTypeDTO removed = hotelService.removeRoomType(roomTypeId);
			inOut.output("Room type removed: room type id - %d, category - %s, price per night - %.2f, capacity - %d"
					.formatted(removed.roomTypeId(), removed.category(), removed.pricePerNight(), removed.capacity()));
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}
	}

}
