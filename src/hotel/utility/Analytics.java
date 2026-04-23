package hotel.utility;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.TreeMap;
import java.util.stream.Stream;

import hotel.interfaces.IAnalytics;
import hotel.model.Booking;
import hotel.model.Room;
import hotel.model.RoomType;
import hotel.validation.Constants;
import hotel.validation.Validation;

public class Analytics implements IAnalytics {
	private List<Booking> bookings;
	private List<Room> rooms;

	public Analytics(Collection<Booking> bookings, Collection<Room> rooms) {
		if (Objects.isNull(bookings))
			this.bookings = new ArrayList<>();
		else
			this.bookings = bookings.stream().filter(booking -> Validation.validateBooking(booking)).toList();
		if (Objects.isNull(rooms))
			this.rooms = new ArrayList<>();
		else
			this.rooms = rooms.stream().filter(room -> Validation.validateRoom(room)).toList();
	}

	@Override
	public int bookingsNumber() {
		return bookings.size();
	}

	@Override
	public double averageBookingPrice() {
		if (bookings.size() == 0)
			return 0.;
		OptionalDouble result = bookings.stream()
				.mapToDouble(booking -> ChronoUnit.DAYS.between(booking.getCheckIn(), booking.getCheckOut())
						* booking.getRoom().getType().getPricePerNight())
				.average();
		return result.getAsDouble();
	}

	@Override
	public Optional<List<RoomType>> mostPopularRoomTypes() {
		return getMostPopularRoomStream(bookings.stream());
	}

	@Override
	public int getAvailableRoomsCount(LocalDate date) {
		return (int) rooms.stream().filter(room -> isAvailable(room, date)).count();
	}

	@Override
	public int getOccupiedRoomsCount(LocalDate date) {
		return (int) rooms.stream().filter(room -> !isAvailable(room, date)).count();
	}

	private boolean isAvailable(Room room, LocalDate date) {
		if (bookings == null || bookings.isEmpty()) {
			return true;
		}
		return bookings.stream().noneMatch(booking -> booking.getRoom().equals(room) && booking.isActiveOn(date));
	}

	@Override
	public Optional<List<RoomType>> getMostPopularRoomTypesForAgeRange(int minAge, int maxAge) {
		if(minAge < Constants.MIN_AGE || minAge > Constants.MAX_AGE || maxAge > Constants.MAX_AGE || maxAge < minAge)
			throw new IllegalArgumentException("invalid age params");
		return getMostPopularRoomStream(bookings.stream().filter(bk -> bk.getGuest().getAge() >= minAge && bk.getGuest().getAge() <= maxAge));
	}

	private Optional<List<RoomType>> getMostPopularRoomStream(Stream<Booking> bookingStream) {
		Map<RoomType, Integer> temp = new HashMap<>();
		bookingStream.forEach(
				booking -> temp.merge(booking.getRoom().getType(), 1, (oldValue, newValue) -> oldValue + newValue));
		TreeMap<Integer, List<RoomType>> result = new TreeMap<>(Comparator.reverseOrder());
		temp.entrySet().stream().forEach(entry -> result.merge(entry.getValue(),
				new ArrayList<RoomType>(Arrays.asList(entry.getKey())), (oldValue, newValue) -> {
					oldValue.addAll(newValue);
					return oldValue;
				}));
		return result.isEmpty() ? Optional.empty() : Optional.of(result.firstEntry().getValue());
	}
}
