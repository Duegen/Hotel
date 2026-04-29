package hotel.items.guest;

import java.util.List;

import hotel.HotelApplContext;
import hotel.items.HotelItem;
import hotel.model.Guest;
import hotel.service.dto.input.GuestLoginDTO;
import hotel.service.dto.output.BookingDTO;

public class ShowMyBookingsItem extends HotelItem {

	protected ShowMyBookingsItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "My bookings list";
	}

	@Override
	public void perform() {
		try {
			GuestLoginDTO login = login();
			Guest guest = hotelService.login(login);
			inOut.outputlLine("Welcome guest '%s'".formatted(guest.getName()));
			List<BookingDTO> bookings = hotelService.getBookingsForGuest(guest.getId());
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
