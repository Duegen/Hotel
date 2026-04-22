package hotel.utility;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.TreeMap;

import hotel.interfaces.IAnalytics;
import hotel.model.Booking;
import hotel.model.RoomType;
import hotel.validation.Validation;

public class Analytics implements IAnalytics {
	private List<Booking> bookings;
	
	public Analytics(List<Booking> bookings) {
		if(Objects.isNull(bookings))
			this.bookings = new ArrayList<Booking>();
		else
			this.bookings = bookings.stream()
				.filter(booking -> Validation.validateBookingId(booking.getBookingId()))
				.filter(booking -> Validation.validateBooking(booking.getGuest(), booking.getRoom(), booking.getCheckIn(), booking.getCheckOut()))
				.toList();
	}

	@Override
	public int bookingsNumber() {
		return bookings.size();
	}
	
	@Override
	public double averageBookingPrice() {
		if(bookings.size() == 0) return 0.;
		OptionalDouble result = bookings.stream().mapToDouble(booking -> 
			ChronoUnit.DAYS.between(booking.getCheckIn(), booking.getCheckOut()) 
				* booking.getRoom().getType().getPricePerNight()
			)
			.average();
		return result.getAsDouble();
	}
	
	@Override
	public List<RoomType> mostPopularRoomTypes() {
		Map<RoomType, Integer> temp = new HashMap<>();
		bookings.stream().forEach(booking -> 
			temp.merge(booking.getRoom().getType(), 1, (oldValue, newValue) -> oldValue+newValue));
		TreeMap<Integer, List<RoomType>> result = new TreeMap<>(Comparator.reverseOrder());
		temp.entrySet().stream().forEach(entry ->
				result.merge(entry.getValue(), new ArrayList<RoomType>(Arrays.asList(entry.getKey())), 
						(oldValue, newValue) -> {
							oldValue.addAll(newValue);
							return oldValue;
						}));
		return result.firstEntry().getValue();
	}
	
	@Override
	public int occupiedRooms() {
		int count = 0;
		for (Booking booking : bookings) {
			if(booking.getCheckIn().isBefore(LocalDate.now()) 
					&& booking.getCheckOut().isAfter(LocalDate.now()))
				count++;
		}
		return count;
	}
}
