package hotel.service.dto.input;

import java.time.LocalDate;

public record GuestCreateDTO(
	String name,
	String email,
	LocalDate dateOfBirth, 
	String password) {}
