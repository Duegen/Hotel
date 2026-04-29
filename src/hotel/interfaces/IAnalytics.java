package hotel.interfaces;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import hotel.model.Booking;
import hotel.model.RoomType;

public interface IAnalytics {
	double averageBookingPrice();
	List<RoomType> mostPopularRoomTypes();
	int getAvailableRoomsCount(LocalDate date);
	int getOccupiedRoomsCount(LocalDate date);
	List<RoomType> getMostPopularRoomTypesForAgeRange(int minAge, int maxAge);
	int bookingsNumber();
	Map<Integer, Booking> getBookings();
}