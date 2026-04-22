package hotel.utility;

import java.util.List;

import hotel.interfaces.IAnalytics;
import hotel.model.*;

public class Displayer {
	public static void displayRoomTypes(List<RoomType> roomTypes) {
		System.out.println("-----[Room types list]-----------------------------------");
		roomTypes.stream().forEach(System.out::println);
		System.out.println();
	}
	
	public static void displayRooms(List<Room> rooms) {
		System.out.println("-----[Rooms list]----------------------------------------");
		rooms.stream().forEach(System.out::println);
		System.out.println();
	}
	
	public static void displayGuests(List<Guest> guests) {
		System.out.println("-----[Guests list]----------------------------------------");
		guests.stream().forEach(System.out::println);
		System.out.println();
	}
	
	public static void displayBookings(List<Booking> bookings) {
		System.out.println("-----[Bookings list]-------------------------------------");
		bookings.stream().forEach(System.out::println);
		System.out.println();
	}
	
	public static void displayAnalitics(List<Booking> bookings, int roomsNumber) {
		IAnalytics analitics = new Analytics(bookings);
		System.out.println("-----[Analitics]-----------------------------------------");
		System.out.println("Total number of bookings: " + analitics.bookingsNumber());
		System.out.println("Average booking price: %.2f".formatted(analitics.averageBookingPrice()));
		System.out.println("The most popular room types: ");
		analitics.mostPopularRoomTypes().stream()
			.forEach(roomType -> System.out.println("-" + roomType));
		int available = roomsNumber - analitics.occupiedRooms();
		System.out.println("Number of availble rooms: " + available);
		int unAvailable = analitics.occupiedRooms();
		System.out.println("Number of occupied rooms: " + unAvailable);
	}
}
