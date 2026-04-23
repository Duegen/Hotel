package hotel.utility;

import java.time.LocalDate;
import java.util.Collection;

import hotel.interfaces.IAnalytics;
import hotel.model.*;

public class Displayer {
	public static void displayRoomTypes(Collection<RoomType> roomTypes) {
		System.out.println("-----[Room types list]-----------------------------------");
		roomTypes.stream().forEach(System.out::println);
		System.out.println();
	}
	
	public static void displayRooms(Collection<Room> rooms) {
		System.out.println("-----[Rooms list]----------------------------------------");
		rooms.stream().forEach(System.out::println);
		System.out.println();
	}
	
	public static void displayGuests(Collection<Guest> guests) {
		System.out.println("-----[Guests list]----------------------------------------");
		guests.stream().forEach(System.out::println);
		System.out.println();
	}
	
	public static void displayBookings(Collection<Booking> bookings) {
		System.out.println("-----[Bookings list]-------------------------------------");
		bookings.stream().forEach(System.out::println);
		System.out.println();
	}
	
	public static void displayAnalitics(Collection<Booking> bookings, Collection<Room> rooms) {
		IAnalytics analytics = new Analytics(bookings, rooms);
		System.out.println("-----[Analitics]-----------------------------------------");
		System.out.println("Total number of bookings: " + analytics.bookingsNumber());
		System.out.println("Average booking price: %.2f".formatted(analytics.averageBookingPrice()));
		System.out.println("The most popular room types: ");
		analytics.mostPopularRoomTypes().stream()
			.forEach(roomType -> System.out.println("-" + roomType));
		int available = analytics.getAvailableRoomsCount(LocalDate.now());
		System.out.println("Number of availble rooms: " + available);
		int unAvailable = analytics.getOccupiedRoomsCount(LocalDate.now());
		System.out.println("Number of occupied rooms: " + unAvailable);
	}
}
