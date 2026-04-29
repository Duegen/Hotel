package hotel.items.manager;

import java.util.List;

import hotel.HotelApplContext;
import hotel.items.HotelItem;
import hotel.service.dto.output.RoomDTO;

public class ShowRoomsItem extends HotelItem {

	protected ShowRoomsItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "Show rooms";
	}

	@Override
	public void perform() {
		inOut.outputlLine("Rooms list:");
		List<RoomDTO> rooms = hotelService.getRoomsDTO();
		showRooms(rooms, "no rooms are found");
	}

}
