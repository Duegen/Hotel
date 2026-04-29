package hotel.items.manager;

import java.util.List;

import hotel.HotelApplContext;
import hotel.items.HotelItem;
import hotel.service.dto.input.GuestIdDTO;
import hotel.service.dto.output.GuestDTO;

public class RemoveGuestItem extends HotelItem {

	protected RemoveGuestItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "Remove guest";
	}

	@Override
	public void perform() {
		try {
			List<GuestDTO> guests = hotelService.getGuestsDTO();
			inOut.outputlLine("Guests list:");
			showGuests(guests, "no guests are found");
			GuestIdDTO guestId = getExistingGuest();
			GuestDTO removed = hotelService.removeGuest(guestId);
			inOut.output("Guest removed: " + removed);
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}
	}

}
