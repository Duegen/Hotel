package hotel.service.dto.output;

public record RoomDTO(
		int RoomNumber,
		String category,
		double pricePerNight,
		int capacity) {}
