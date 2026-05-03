package hotel.items.accountant;

import java.time.LocalDate;

import hotel.HotelApplConstants;
import hotel.HotelApplContext;
import hotel.items.HotelItem;

public class ShowBookingsByCheckInItem extends HotelItem {

	public ShowBookingsByCheckInItem(HotelApplContext context) {
		super(context);
	}


	@Override
	public String displayName() {
		return "Show bookings for selected day";
	}


	@Override
	public void perform() {
		LocalDate selected = inOut.inputDate("Enter date in format", HotelApplConstants.DATE_FORMAT);
		if(selected==null)
			return;
		showFullBookings(context.getHotelService().getBookingsDTO().stream().filter(bk -> bk.checkIn().equals(selected)).toList(), "No bookings found for "+ selected);
		
	}

}
