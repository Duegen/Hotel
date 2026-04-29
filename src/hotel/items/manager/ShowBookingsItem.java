package hotel.items.manager;

import java.util.List;

import hotel.HotelApplContext;

import hotel.items.HotelItem;
import hotel.service.dto.output.BookingFullDTO;

public class ShowBookingsItem extends HotelItem {
    public ShowBookingsItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Show bookings";
    }

    @Override
    public void perform() {
    	inOut.outputlLine("Bookings list:");
		List<BookingFullDTO> bookings = hotelService.getBookingsDTO();
		showFullBookings(bookings, "no rooms are found");
    }
}
