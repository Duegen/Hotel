package hotel.items.manager;

import hotel.HotelApplContext;
import hotel.items.HotelItem;
import hotel.service.dto.input.RoomTypeCreateDTO;
import hotel.service.dto.output.RoomTypeDTO;

public class AddRoomTypeItem extends HotelItem {

	public AddRoomTypeItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "Add new room type";
	}

	@Override
	public void perform() {
		try {
			RoomTypeCreateDTO newRoomType = addRoomType();
			RoomTypeDTO createdRoomType = hotelService.createRoomType(newRoomType);
			inOut.outputlLine("Room type created: category - %s, price per night - %.2f, capacity - %d"
					.formatted(createdRoomType.category(), createdRoomType.pricePerNight(), createdRoomType.capacity()));
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}
		
	}

}
