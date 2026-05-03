package hotel.items.login;

import java.util.Map.Entry;

import hotel.HotelApplContext;
import hotel.items.HotelItem;
import hotel.service.dto.input.GuestLoginDTO;

public class LoginItem extends HotelItem{

	public LoginItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "Login";
	}

	@Override
	public void perform() {
		try {
			GuestLoginDTO loginDTO = login();
			Entry<Integer, Integer> login = hotelService.login(loginDTO);
			context.setExcessLevel(login.getValue());
			context.setGuestId(login.getKey());
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}
	}
	
	@Override
	public boolean isExit() {
		if(context.getExcessLevel() != -1)
			return true;
		else return false;
	}
}
