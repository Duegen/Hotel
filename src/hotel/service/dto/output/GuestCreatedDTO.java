package hotel.service.dto.output;

import java.time.LocalDate;

public record GuestCreatedDTO(
		String name,
		String email,
		LocalDate dateOfBirth) {

}
