package hotel.items.guest;

import java.time.LocalDate;
import java.util.List;

import hotel.HotelApplContext;

import hotel.items.HotelItem;
import hotel.service.dto.input.BookingIdDTO;
import hotel.service.dto.output.BookingDTO;

public class CancelMyBookingItem extends HotelItem {
    public CancelMyBookingItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Cancel my booking";
    }

    @Override
    public void perform() {
    	try {
			inOut.outputlLine("Your bookings:");
			List<BookingDTO> activeBookings = hotelService.getBookingsForGuest(context.getGuestId())
					.stream().filter(bk -> LocalDate.now().isBefore(bk.checkIn())).toList();
			if (activeBookings.isEmpty()) {
			    inOut.outputlLine("No bookings available for canceling are found");
			    return;
			}
			showBookings(activeBookings, "No bookings available for canceling are found");

			Integer bookingId = inOut.inputInteger("Enter booking id to cancel");
			
			BookingDTO removed = hotelService.cancelBooking(new BookingIdDTO(bookingId));
			inOut.outputlLine("Booking canceled: room number - %d, category - %s, price per night - %.2f, capacity - %d, check in - %s, check out - %s"
					.formatted(removed.roomNumber(), removed.caregory(), removed.pricePerNight(), removed.capacity(), removed.checkIn(), removed.checkOut()));
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}
    }
}
