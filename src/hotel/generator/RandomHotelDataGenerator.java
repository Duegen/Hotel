package hotel.generator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import hotel.HotelApplContext;
import hotel.interfaces.IHotelService;
import hotel.model.*;
import hotel.service.dto.input.BookingCreateDTO;
import hotel.service.dto.input.GuestCreateDTO;
import hotel.service.dto.input.RoomCreateDTO;
import hotel.service.dto.input.RoomTypeCreateDTO;
import hotel.service.exceptions.RoomException;
import hotel.service.exceptions.RoomTypeException;
import hotel.validation.exceptions.ValidationException;

public class RandomHotelDataGenerator {
	private final IHotelService service;
	private static final int N_TYPES = 10;
	private final static int N_GUESTS = Constants.NAMES_EMAILS.size();
	private final static int N_BOOKINGS = 20;
	
	private static Random r = new Random();
	
	public RandomHotelDataGenerator(HotelApplContext context) {
		service = context.getHotelService();
	}
	
	private void generateRoomTypes() {
		while(service.getRoomTypes().size() < N_TYPES) {
			try {
				service.createRoomType(getRandomRoomType());
			} catch (Exception e) {
				continue;
			}
		}
	}

	private RoomTypeCreateDTO getRandomRoomType() {
		int randomNameIndex = r.nextInt(0, 3);
		String randomName = Constants.NAMES[randomNameIndex];
		int randomCapacity = Constants.CAPACITY[r.nextInt(0, 4)];
		double randomPrice = Constants.PRICE_PER_PERSON_PER_NIGHT[randomNameIndex];
		RoomTypeCreateDTO dto = new RoomTypeCreateDTO(randomName, randomPrice * randomCapacity, randomCapacity);
		return dto;
	}
	
	private void generateRooms(){
		for (int number : Constants.ROOM_NUMBERS)
			try {
				service.createRoom(getRandomRoom(number));
			} catch (RoomException | RoomTypeException | ValidationException e) {
				continue;
			}
	}

	private RoomCreateDTO getRandomRoom(int number) {
		Collection<RoomType> roomTypes = service.getRoomTypes().values(); 
		int randomTypeId = roomTypes.stream().skip(r.nextInt(0, roomTypes.size()))
				.findFirst().orElse(null).getTypeId();
		return new RoomCreateDTO(number, randomTypeId);
	}
	
	private void generateGuests(){
		Collection<Guest> guests = service.getGuests().values();
		while(guests.size() < N_GUESTS) { 
			GuestCreateDTO randomGuest = getRandomGuest(guests.size());
			try {
				service.createGuest(randomGuest);
			} catch (Exception e) {
				continue;
			}
		}
	}

	private static GuestCreateDTO getRandomGuest(int index) {
		Entry<String, String> randomNameEmail = Constants.NAMES_EMAILS.entrySet().stream()
				.skip(index)
				.findFirst().orElse(null);
		LocalDate randomBD = Constants.MIN_BD.plusDays(r.nextLong(0, ChronoUnit.DAYS.between(Constants.MIN_BD, Constants.MAX_BD)));
		GuestCreateDTO dto = new GuestCreateDTO(randomNameEmail.getKey(),
				randomNameEmail.getValue(), randomBD, "Password1");
		return dto;
	}
	
	private void generateBookings(){
		Collection<Booking> bookings = service.getBookings().values();
		while(bookings.size() < N_BOOKINGS) {
			BookingCreateDTO randomBooking = getRandomBooking();
			try {
				service.createBooking(randomBooking);
			} catch (Exception e) {
				continue;
			}
		}
	}

	private BookingCreateDTO getRandomBooking() {
		Map<Integer, Room> rooms = service.getRooms();
		Map<Integer, Guest> guests = service.getGuests();
		int randomRoomId = rooms.values().stream()
				.skip(r.nextInt(0, rooms.size())).findFirst().orElse(null).getRoomNumber();
		int randomGuestId = guests.values().stream()
				.skip(r.nextInt(0, guests.size())).findFirst().orElse(null).getId();
		LocalDate date = LocalDate.now().minusDays(r.nextInt(1, 100));
		LocalDate checkOut = date.plusDays(r.nextInt(1, 20));
		LocalDate checkIn = checkOut.minusDays(r.nextInt(1, 20));
		return new BookingCreateDTO(randomGuestId, randomRoomId, checkIn, checkOut);
	}
	
	public void generate() {
		generateRoomTypes();
		generateRooms();
		generateGuests();
		generateBookings();
	}
}
