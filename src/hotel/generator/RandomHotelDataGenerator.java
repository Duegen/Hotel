package hotel.generator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import hotel.interfaces.IHotelService;
import hotel.model.Booking;
import hotel.model.Guest;
import hotel.model.Room;
import hotel.model.RoomType;

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
		HashSet<RoomType> result = new HashSet<RoomType>();
		while(result.size() < N_TYPES) {
			result.add(getRandomRoomType());
		}
		return new ArrayList<RoomType>(result);
	}

	private RoomType getRandomRoomType() {
		int randomNameIndex = r.nextInt(0, 3);
		String randomName = Constants.NAMES[randomNameIndex];
		int randomCapacity = Constants.CAPACITY[r.nextInt(0, 4)];
		double randomPrice = Constants.PRICE_PER_PERSON_PER_NIGHT[randomNameIndex];
		return new RoomType(randomName, randomPrice * randomCapacity, randomCapacity);
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
		Set<Guest> result = new HashSet<>();
		while(result.size() < N_GUESTS) 
			result.add(getRandomGuest(result.size()));
		return new ArrayList<Guest>(result);
	}

	private static Guest getRandomGuest(int index) {
		Entry<String, String> randomNameEmail = Constants.NAMES_EMAILS.entrySet().stream()
				.skip(index)
				.findFirst().orElse(null);
		return new Guest(r.nextInt(Constants.GUEST_MIN_ID, Constants.GUEST_MAX_ID + 1), randomNameEmail.getKey(),
				randomNameEmail.getValue(), "password");
	}
	
	private List<Booking> generateBookings(){
		HashSet<Booking> result = new HashSet<>();
		while(result.size() < N_BOOKINGS) {
			Booking newBooking = getRandomBooking();
			
			if(isRoomAvailable(result, newBooking.getRoom(), newBooking.getCheckIn(), newBooking.getCheckOut())) result.add(newBooking);
		}
		return new ArrayList<Booking>(result);
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
	
	private boolean isRoomAvailable(HashSet<Booking> result, Room room, LocalDate checkIn, LocalDate checkOut) {
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
				service.addRoomType(rt.getName(), rt.getPricePerNight(), rt.getCapacity());
			} catch (Exception e) {
				
			}
		});
		rooms.stream().forEach(r -> {
			try {
				service.addRoom(r.getRoomNumber(), r.getType());
			} catch (Exception e) {
				
			}
		});
		guests.stream().forEach(g -> {
			try {
				service.addGuest(g.getId(), g.getName(), g.getEmail(), null);
			} catch (Exception e) {
				
			}
		});
		bookings.stream().forEach(bk -> {
			try {
				service.addBooking(bk);
			} catch (Exception e) {
			}
		});
	}
}
