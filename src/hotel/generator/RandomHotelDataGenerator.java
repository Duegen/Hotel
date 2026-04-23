package hotel.generator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.Random;

import hotel.interfaces.IHotelService;
import hotel.model.*;

public class RandomHotelDataGenerator {
	private List<RoomType> roomTypes;
	private List<Room> rooms;
	private List<Guest> guests;
	private List<Booking> bookings;
	
	private static final int N_TYPES = 10;
	private final static int N_GUESTS = Constants.NAMES_EMAILS.size();
	private final static int N_BOOKINGS = 20;
	
	private static Random r = new Random();
	
	public RandomHotelDataGenerator() {
		this.roomTypes = generateRoomTypes();
		this.rooms = generateRooms();
		this.guests = generateGuests();
		this.bookings = generateBookings();
	}
	
	private List<RoomType> generateRoomTypes() {
		List<RoomType> result = new ArrayList<RoomType>();
		while(result.size() < N_TYPES) {
			RoomType randomType = getRandomRoomType();
			if(result.stream().noneMatch(rt -> rt.equals(randomType))
					&& result.stream().noneMatch(rt -> rt.isSame(randomType)))
				result.add(randomType);
		}
		return result;
	}

	private RoomType getRandomRoomType() {
		int randomId = r.nextInt(Constants.TYPE_MIN_ID, Constants.TYPE_MAX_ID + 1);
		int randomNameIndex = r.nextInt(0, 3);
		String randomName = Constants.NAMES[randomNameIndex];
		int randomCapacity = Constants.CAPACITY[r.nextInt(0, 4)];
		double randomPrice = Constants.PRICE_PER_PERSON_PER_NIGHT[randomNameIndex];
		return new RoomType(randomId, randomName, randomPrice * randomCapacity, randomCapacity);
	}
	
	private List<Room> generateRooms(){
		ArrayList<Room> result = new ArrayList<Room>();
		for (int number : Constants.ROOM_NUMBERS) 
			result.add(getRandomRoom(number));
		return result;
	}

	private Room getRandomRoom(int number) {
		RoomType randomType = roomTypes.stream().skip(r.nextInt(0, roomTypes.size()))
				.findFirst().orElse(null);
		return new Room(number, randomType);
	}
	
	private List<Guest> generateGuests(){
		List<Guest> result = new ArrayList<>();
		while(result.size() < N_GUESTS) { 
			Guest randomGuest = getRandomGuest(result.size());
			if(result.stream().noneMatch(g -> g.equals(randomGuest)))
				result.add(randomGuest);
		}
		return result;
	}

	private static Guest getRandomGuest(int index) {
		Entry<String, String> randomNameEmail = Constants.NAMES_EMAILS.entrySet().stream()
				.skip(index)
				.findFirst().orElse(null);
		LocalDate randomBD = Constants.MIN_BD.plusDays(r.nextLong(0, ChronoUnit.DAYS.between(Constants.MIN_BD, Constants.MAX_BD)));
		return new Guest(r.nextInt(Constants.GUEST_MIN_ID, Constants.GUEST_MAX_ID + 1), randomNameEmail.getKey(),
				randomNameEmail.getValue(), randomBD, "password");
	}
	
	private List<Booking> generateBookings(){
		List<Booking> result = new ArrayList<>();
		while(result.size() < N_BOOKINGS) {
			Booking randomBooking = getRandomBooking();
			boolean isAvailable = isRoomAvailable(result, randomBooking.getRoom(), randomBooking.getCheckIn(), randomBooking.getCheckOut());
			if(isAvailable && result.stream().noneMatch(bk -> bk.equals(randomBooking))) 
				result.add(randomBooking);
		}
		return result;
	}

	private Booking getRandomBooking() {
		int id = r.nextInt(Constants.BOOKING_MIN_ID, Constants.BOOKING_MAX_ID + 1);
		Room randomRoom = rooms.get(r.nextInt(0, rooms.size()));
		Guest randomGuest = guests.get(r.nextInt(0, guests.size()));
		LocalDate date = LocalDate.now().minusDays(r.nextInt(1, 100));
		LocalDate checkOut = date.plusDays(r.nextInt(1, 20));
		LocalDate checkIn = checkOut.minusDays(r.nextInt(1, 20));
		return new Booking(id, randomGuest, randomRoom, checkIn, checkOut);
	}
	
	public List<RoomType> getRoomTypes() {
		return roomTypes;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public List<Guest> getGuests() {
		return guests;
	}

	public List<Booking> getBookings() {
		return bookings;
	}
	
	private boolean isRoomAvailable(List<Booking> result, Room room, LocalDate checkIn, LocalDate checkOut) {
        if(Objects.isNull(room) || Objects.isNull(checkIn) || Objects.isNull(checkOut))
        	throw new IllegalArgumentException("arg can not be null");
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out must be after check-in");
        }
        return result.stream()
                .filter(booking -> booking.getRoom().equals(room))
                .noneMatch(booking -> booking.overlaps(checkIn, checkOut));
    }
	
	public void copy(IHotelService service) {
		roomTypes.stream().forEach(rt -> {
			try {
				service.addRoomType(rt);
			} catch (Exception e) {
				System.out.println(e);
			}
		});
		rooms.stream().forEach(r -> {
			try {
				service.addRoom(r.getRoomNumber(), r.getType());
			} catch (Exception e) {
				System.out.println(e);
			}
		});
		guests.stream().forEach(g -> {
			try {
				service.addGuest(g);
			} catch (Exception e) {
				System.out.println(e);
			}
		});
		bookings.stream().forEach(bk -> {
			try {
				service.addBooking(bk);
			} catch (Exception e) {
				System.out.println(e);
			}
		});
	}
}
