package hotel.interfaces;

import java.time.LocalDate;
import java.util.List;
import hotel.model.RoomType;

public interface IAnalytics {
	int bookingsNumber();
	double averageBookingPrice();
	List<RoomType> mostPopularRoomTypes();
	int getAvailableRoomsCount(LocalDate date);
	int getOccupiedRoomsCount(LocalDate date);
}