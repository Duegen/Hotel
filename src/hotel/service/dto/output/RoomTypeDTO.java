package hotel.service.dto.output;

public record RoomTypeDTO(
		int roomTypeId,
		String category,
		double pricePerNight,
		int capacity) {}
