package hotel.interfaces;

import java.util.List;

import hotel.model.RoomType;

public interface IAnalytics {
	int bookingsNumber();
	double averageBookingPrice();
	List<RoomType> mostPopularRoomTypes();
	int occupiedRooms();
}