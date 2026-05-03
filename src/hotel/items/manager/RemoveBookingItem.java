package hotel.items.manager;

import java.util.List;

import hotel.HotelApplContext;

import hotel.items.HotelItem;
import hotel.model.Guest;
import hotel.model.RoomType;
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
			Guest guest = removed.guest();
			RoomType rt = removed.roomType();
			inOut.output("Booking removed: booking id - %d, guest id - %d, guest name - %s, guest email - %s, room number - %d, category - %s, price per night - %.2f, capacity - %d, check in - %s, check out - %s"
					.formatted(removed.BookingId(), guest.getId(), guest.getName(), guest.getEmail(), removed.roomNumber(), rt.getCategory(), rt.getPricePerNight(), rt.getCapacity(), removed.checkIn().toString(), removed.checkOut().toString()));
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}
    }
}
