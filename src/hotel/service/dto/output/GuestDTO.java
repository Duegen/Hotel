package hotel.service.dto.output;

import java.time.LocalDate;

public record GuestDTO(
		int guestId,
		String name,
		String email,
		LocalDate dateOfBirth) {}
