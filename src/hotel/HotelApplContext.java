package hotel;

import cli.Inputoutput;
import hotel.interfaces.IAnalytics;
import hotel.interfaces.IBookingsPersistence;
import hotel.interfaces.IGuestsPersistence;
import hotel.interfaces.IHotelService;
import hotel.interfaces.IRoomTypesPersistence;
import hotel.interfaces.IRoomsPersistence;

public class HotelApplContext {
	private final Inputoutput inOut;
	private final IHotelService hotelService;
	private final IAnalytics analyticsService;
	private final IRoomTypesPersistence roomTypePersistence;
	private final IRoomsPersistence roomPersistence;
	private final IGuestsPersistence guestPersistence;
	private final IBookingsPersistence bookingPersistence;
	
	public HotelApplContext(Inputoutput inOut, IHotelService hotelService, IAnalytics analyticsService,
			IRoomTypesPersistence roomTypePersistence, IRoomsPersistence roomPersistence,
			IGuestsPersistence guestPersistence, IBookingsPersistence bookingPersistence) {
		this.inOut = inOut;
		this.hotelService = hotelService;
		this.analyticsService = analyticsService;
		this.roomTypePersistence = roomTypePersistence;
		this.roomPersistence = roomPersistence;
		this.guestPersistence = guestPersistence;
		this.bookingPersistence = bookingPersistence;
	}

	public Inputoutput getInOut() {
		return inOut;
	}

	public IHotelService getHotelService() {
		return hotelService;
	}

	public IRoomTypesPersistence getRoomTypePersistence() {
		return roomTypePersistence;
	}

	public IRoomsPersistence getRoomPersistence() {
		return roomPersistence;
	}

	public IGuestsPersistence getGuestPersistence() {
		return guestPersistence;
	}

	public IBookingsPersistence getBookingPersistence() {
		return bookingPersistence;
	}

	public IAnalytics getAnalyticsService() {
		return analyticsService;
	}
}
