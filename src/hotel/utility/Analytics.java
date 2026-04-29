package hotel.utility;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hotel.interfaces.IAnalytics;
import hotel.interfaces.IHotelService;
import hotel.model.*;
import hotel.validation.Constants;

public class Analytics implements IAnalytics {
	private final Map<Integer, RoomType> roomTypes;
	private final Map<Integer, Room> rooms;
	private final Map<Integer, Guest> guests;
	private final Map<Integer, Booking> bookings;
	
	public Analytics(IHotelService service) {
		roomTypes = service.getRoomTypes();
		rooms = service.getRooms();
		guests = service.getGuests();
		bookings = service.getBookings();
	}

	
	
	public Map<Integer, RoomType> getRoomTypes() {
		return roomTypes;
	}



	public Map<Integer, Room> getRooms() {
		return rooms;
	}



	public Map<Integer, Guest> getGuests() {
		return guests;
	}


	@Override
	public Map<Integer, Booking> getBookings() {
		return bookings;
	}



	@Override
	public int bookingsNumber() {
		return bookings.size();
	}

	@Override
	public double averageBookingPrice() {
		if (bookings.size() == 0)
			return 0.;
		OptionalDouble result = bookings.values().stream()
				.mapToDouble(booking -> ChronoUnit.DAYS.between(booking.getCheckIn(), booking.getCheckOut())
						* roomTypes.get(rooms.get(booking.getRoomNumber()).getType()).getPricePerNight())
				.average();
		return result.getAsDouble();
	}

	@Override
	public List<RoomType> mostPopularRoomTypes() {
		return getMostPopularRoomStream(bookings.values().stream());
	}

	@Override
	public List<RoomType> getMostPopularRoomTypesForAgeRange(int minAge, int maxAge) {
		if (minAge < Constants.MIN_AGE || minAge > Constants.MAX_AGE || maxAge > Constants.MAX_AGE || maxAge < minAge)
			throw new IllegalArgumentException("invalid age params");
		return getMostPopularRoomStream(
				bookings.values().stream().filter(bk -> guests.get(bk.getGuestId()).getAge() >= minAge
						&& guests.get(bk.getGuestId()).getAge() <= maxAge));
	}

	private List<RoomType> getMostPopularRoomStream(Stream<Booking> bookingStream) {
		Map<Integer, Long> counts = bookingStream.collect(
				Collectors.groupingBy(booking -> rooms.get(booking.getRoomNumber()).getType(), Collectors.counting()));
		long max = counts.values().stream().mapToLong(Long::longValue).max().orElse(0);
		return counts.entrySet().stream().filter(entry -> entry.getValue() == max)
				.map(entry -> roomTypes.get(entry.getKey())).toList();
	}
	
	@Override
	public int getAvailableRoomsCount(LocalDate date) {
		return (int) rooms.values().stream().filter(room -> isAvailable(room, date)).count();
	}

	@Override
	public int getOccupiedRoomsCount(LocalDate date) {
		return (int) rooms.values().stream().filter(room -> !isAvailable(room, date)).count();
	}

	private boolean isAvailable(Room room, LocalDate date) {
		if (bookings == null || bookings.isEmpty()) {
			return true;
		}
		return bookings.values().stream().noneMatch(booking -> booking.getRoomNumber() == room.getRoomNumber() && booking.isActiveOn(date));
	}

	
}
