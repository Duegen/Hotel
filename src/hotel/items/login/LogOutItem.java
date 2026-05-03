package hotel.items.login;

import hotel.HotelApplContext;
import hotel.items.HotelItem;

public class LogOutItem extends HotelItem{

	public LogOutItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "Log out";
	}

	@Override
	public void perform() {
		context.setExcessLevel(-1);	
	}

	
	@Override
	public boolean isExit() {
		return true;
	}
}
