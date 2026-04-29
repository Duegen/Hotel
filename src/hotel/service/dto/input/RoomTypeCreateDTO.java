package hotel.service.dto.input;

public record RoomTypeCreateDTO(
	String category,
	double pricePerNight,
	int capacity) {}
