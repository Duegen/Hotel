package hotel.interfaces;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import hotel.model.RoomType;

public interface IAnalytics {
	int bookingsNumber();
	double averageBookingPrice();
	Optional<List<RoomType>> mostPopularRoomTypes();
	int getAvailableRoomsCount(LocalDate date);
	int getOccupiedRoomsCount(LocalDate date);
	Optional<List<RoomType>> getMostPopularRoomTypesForAgeRange(int minAge, int maxAge);
}