package hotel.interfaces;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import hotel.model.Booking;
import hotel.service.dto.output.RoomTypeDTO;

public interface IAnalytics {
	double averageBookingPrice();
	List<RoomTypeDTO> mostPopularRoomTypes();
	int getAvailableRoomsCount(LocalDate date);
	int getOccupiedRoomsCount(LocalDate date);
	List<RoomTypeDTO> getMostPopularRoomTypesForAgeRange(int minAge, int maxAge);
	int bookingsNumber();
	Map<Integer, Booking> getBookings();
}