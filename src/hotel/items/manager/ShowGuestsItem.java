package hotel.items.manager;

import java.util.List;

import hotel.HotelApplContext;
import hotel.items.HotelItem;
import hotel.service.dto.output.GuestDTO;

public class ShowGuestsItem extends HotelItem {

	protected ShowGuestsItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "Show guests";
	}

	@Override
	public void perform() {
		inOut.outputlLine("Guests list:");
		List<GuestDTO> guests = hotelService.getGuestsDTO();
		showGuests(guests, "no guests are found");
	}

}
