package hotel.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Booking implements Serializable{
	private static final long serialVersionUID = 1L;
	private int bookingId;
	private Guest guest;
	private Room room;
	private LocalDate checkIn;
	private LocalDate checkOut;
	
	public Booking(int bookingId, Guest guest, Room room, LocalDate checkIn, LocalDate checkOut) {
		if (Objects.isNull(guest)) 
            throw new IllegalArgumentException("Guest can not be null");
        if (Objects.isNull(room)) 
            throw new IllegalArgumentException("Room can not be null");
        if (Objects.isNull(checkIn) || Objects.isNull(checkOut)) 
            throw new IllegalArgumentException("Check-in and check-out dates can not be null");
        if (!checkOut.isAfter(checkIn)) 
            throw new IllegalArgumentException("Check-out must be after check-in");
        if(bookingId <= 0)
        	throw new IllegalArgumentException("Booking id must be positive");
		this.bookingId = bookingId;
		this.guest = guest;
		this.room = room;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
	}

	@Override
	public String toString() {
		return "Booking [bookingId=" + bookingId + ", guest=" + guest + ", room=" + room + ", checkIn=" + checkIn
				+ ", checkOut=" + checkOut + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(bookingId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Booking other)) {
            return false;
        }
		return bookingId == other.bookingId;
	}

	public int getBookingId() {
		return bookingId;
	}

	public Guest getGuest() {
		return guest;
	}

	public Room getRoom() {
		return room;
	}

	public LocalDate getCheckIn() {
		return checkIn;
	}

	public LocalDate getCheckOut() {
		return checkOut;
	}
	
	public boolean isActiveOn(LocalDate date) {
        return (date.isEqual(checkIn) || date.isAfter(checkIn)) && date.isBefore(checkOut);
    }
	
	public boolean overlaps(LocalDate requestedCheckIn, LocalDate requestedCheckOut) {
        return requestedCheckIn.isBefore(checkOut) && requestedCheckOut.isAfter(checkIn);
    }

}
