package hotel.items.login;

import hotel.HotelApplContext;
import hotel.items.HotelItem;
import hotel.service.dto.input.GuestCreateDTO;

public class RegisterGuestItem extends HotelItem {

	public RegisterGuestItem(HotelApplContext context) {
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
			hotelService.createGuest(guestDTO);
			inOut.outputlLine("Guest registered");
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}

	}

}
