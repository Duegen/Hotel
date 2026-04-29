package hotel.utility;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import hotel.interfaces.IAnalytics;
import hotel.interfaces.IHotelService;
import hotel.model.*;

public class Displayer {
	
	public static void displayRoomTypes(Collection<RoomType> roomTypes) {
		System.out.println("-----[Room types list]-----------------------------------");
		if(roomTypes.isEmpty())
			System.out.println("room type list is empty");
		else
			roomTypes.stream().forEach(System.out::println);
		System.out.println();
	}
	
	public static void displayRooms(Collection<Room> rooms) {
		System.out.println("-----[Rooms list]----------------------------------------");
		if(rooms.isEmpty())
			System.out.println("room list is empty");
		else
			rooms.stream().forEach(System.out::println);
		System.out.println();
	}
	
	public static void displayGuests(Collection<Guest> guests) {
		System.out.println("-----[Guests list]----------------------------------------");
		if(guests.isEmpty())
			System.out.println("room type list is empty");
		else
			guests.stream().forEach(System.out::println);
		System.out.println();
	}
	
	public static void displayBookings(Collection<Booking> bookings) {
		System.out.println("-----[Bookings list]-------------------------------------");
		if(bookings.isEmpty())
			System.out.println("booking list is empty");
		else
			bookings.stream().forEach(System.out::println);
		System.out.println();
	}
	
	public static void displayAnalitics(IHotelService service) {
		IAnalytics analytics = new Analytics(service);
		System.out.println("-----[Analitics]-----------------------------------------");
		System.out.println("Total number of bookings: " + analytics.bookingsNumber());
		System.out.println("Average booking price: %.2f".formatted(analytics.averageBookingPrice()));
		System.out.println("The most popular room types: ");
		if(analytics.getBookings().size() != 0)
			analytics.mostPopularRoomTypes().stream()
				.forEach(roomType -> System.out.println("-" + roomType));
		else
			System.out.println("-no suitable room types are found");
		int minAge = 25, maxAge = 45;
		System.out.println("The most popular room types in age range %d-%d:".formatted(minAge, maxAge));
		List<RoomType> roomTypesInAgeRange = analytics.getMostPopularRoomTypesForAgeRange(minAge, maxAge);
		if(roomTypesInAgeRange.isEmpty())
			System.out.println("-no suitable room types are found");
		else	
			roomTypesInAgeRange.stream()
				.forEach(roomType -> System.out.println("-" + roomType));
		int available = analytics.getAvailableRoomsCount(LocalDate.now());
		System.out.println("Number of availble rooms on date %s: %d".formatted(LocalDate.now(), available));
		int unAvailable = analytics.getOccupiedRoomsCount(LocalDate.now());
		System.out.println("Number of occupied rooms on date %s: %d".formatted(LocalDate.now(), unAvailable));
		System.out.println();
	}
}
