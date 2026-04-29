package hotel.items.manager;

import java.util.List;

import hotel.HotelApplContext;

import hotel.items.HotelItem;
import hotel.service.dto.input.BookingIdDTO;
import hotel.service.dto.output.BookingFullDTO;

public class RemoveBookingItem extends HotelItem {
    public RemoveBookingItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Remove booking";
    }

    @Override
    public void perform() {
        try {
			inOut.outputlLine("Bookings list:");
			List<BookingFullDTO> bookings = hotelService.getBookingsDTO();
			showFullBookings(bookings, "no bookings are found");
			BookingIdDTO bookingId = getExistingBooking();
			BookingFullDTO removed = hotelService.removeBooking(bookingId);
			inOut.output("Booking removed: " + removed);
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}
    }
}
