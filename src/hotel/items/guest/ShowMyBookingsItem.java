package hotel.items.guest;

import java.util.List;

import hotel.HotelApplContext;
import hotel.items.HotelItem;
import hotel.service.dto.output.BookingDTO;

public class ShowMyBookingsItem extends HotelItem {

	public ShowMyBookingsItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "My bookings list";
	}

	@Override
	public void perform() {
		try {
			List<BookingDTO> bookings = hotelService.getBookingsForGuest(context.getGuestId());
			if (bookings.isEmpty()) {
			    inOut.outputlLine("No bookings found ");
			    return;
			}
			inOut.outputlLine("Your bookings:");
			showBookings(bookings, "No bookings are found");
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		} 
	}

}
