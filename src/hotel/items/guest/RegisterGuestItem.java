package hotel.items.guest;

import hotel.HotelApplContext;
import hotel.items.HotelItem;
import hotel.service.dto.input.GuestCreateDTO;
import hotel.service.dto.output.GuestCreatedDTO;

public class RegisterGuestItem extends HotelItem {

	protected RegisterGuestItem(HotelApplContext context) {
		super(context);

	}

	@Override
	public String displayName() {
		return "Register new guest";
	}

	@Override
	public void perform() {
		try {
			GuestCreateDTO guestDTO = inputNewGuest();
			GuestCreatedDTO newGuest = hotelService.createGuest(guestDTO);
			inOut.outputlLine("Guest registered: " + newGuest);
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}

	}

}
