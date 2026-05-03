package hotel.items.accountant;

import java.time.LocalDate;
import java.util.List;

import hotel.HotelApplContext;
import hotel.items.HotelItem;
import hotel.service.dto.output.RoomTypeDTO;

public class ShowAllStatisticsItem extends HotelItem {

	public ShowAllStatisticsItem(HotelApplContext context) {
		super(context);
		
	}

	@Override
	public String displayName() {
		return "Show all statistics";
	}

	@Override
	public void perform() {
		LocalDate today = LocalDate.now();
		inOut.outputlLine("Room types count: "+ hotelService.getRoomTypes().size());
		inOut.outputlLine("Rooms count: "+ hotelService.getRooms().size());
		inOut.outputlLine("Guests count: "+ hotelService.getGuests().size());
		inOut.outputlLine("Bookings count: "+ hotelService.getBookings().size());
		inOut.outputlLine("Average booking price: "+ analyticsService.averageBookingPrice());
		List<RoomTypeDTO> popularRT = analyticsService.mostPopularRoomTypes();
		inOut.outputlLine("Most popular room types:");
		popularRT.stream().forEach(rt -> inOut.outputlLine("room type id - %d, category - %s, price per night - %.2f, capacity - %d"
				.formatted(rt.roomTypeId(), rt.category(), rt.pricePerNight(), rt.capacity())));
		inOut.outputlLine("Available rooms today: "+ 
		analyticsService.getAvailableRoomsCount(today));
		inOut.outputlLine("Occupied rooms today: "+ 
				analyticsService.getOccupiedRoomsCount(today));
	}


}
