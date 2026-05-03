package hotel;

import java.nio.file.Files;
import java.nio.file.Path;
import cli.ConsoleInputOutput;
import cli.ExitItem;
import cli.Inputoutput;
import cli.Item;
import cli.Menu;
import hotel.generator.RandomHotelDataGenerator;
import hotel.interfaces.IAnalytics;
import hotel.interfaces.IBookingsPersistence;
import hotel.interfaces.IGuestsPersistence;
import hotel.interfaces.IHotelPersistenceService;
import hotel.interfaces.IHotelService;
import hotel.interfaces.IRoomTypesPersistence;
import hotel.interfaces.IRoomsPersistence;
import hotel.items.SaveAndExitItem;
import hotel.items.accountant.ShowAllStatisticsItem;
import hotel.items.accountant.ShowBookingsByCheckInItem;
import hotel.items.accountant.ShowMostPopularRoomForRangeAgeItem;
import hotel.items.accountant.ShowOccupancyForDateItem;
import hotel.items.guest.CancelMyBookingItem;
import hotel.items.guest.CreateBookingItem;
import hotel.items.guest.ShowAvailableRoomsItem;
import hotel.items.guest.ShowMyBookingsItem;
import hotel.items.login.LogOutItem;
import hotel.items.login.LoginItem;
import hotel.items.login.RegisterGuestItem;
import hotel.items.manager.AddRoomItem;
import hotel.items.manager.AddRoomTypeItem;
import hotel.items.manager.RemoveBookingItem;
import hotel.items.manager.RemoveGuestItem;
import hotel.items.manager.RemoveRoomItem;
import hotel.items.manager.RemoveRoomTypeItem;
import hotel.items.manager.ShowBookingsItem;
import hotel.items.manager.ShowGuestsItem;
import hotel.items.manager.ShowRoomTypesItem;
import hotel.items.manager.ShowRoomsItem;
import hotel.persistence.BookingsPersistenceEmbeddedImp;
import hotel.persistence.GuestsPersistenceEmbeddedImp;
import hotel.persistence.RoomTypesPersistenceEmbeddedImp;
import hotel.persistence.RoomsPersistenceEmbeddedImp;
import hotel.service.HotelPersistenceService;
import hotel.service.HotelService;
import hotel.utility.Analytics;

public class HotelServiceAppl {

	public static void main(String[] args) throws Exception {
		Inputoutput inOut = new ConsoleInputOutput();
		IHotelService service = HotelService.getInstance();
		IAnalytics analytics = new Analytics(service);
		IRoomTypesPersistence roomTypePersistence = RoomTypesPersistenceEmbeddedImp.getInstance();
		IRoomsPersistence roomPersistence = RoomsPersistenceEmbeddedImp.getInstance();
		IGuestsPersistence guestPersistence = GuestsPersistenceEmbeddedImp.getInstance();
		IBookingsPersistence bookingPersistence = BookingsPersistenceEmbeddedImp.getInstance();
		IHotelPersistenceService hotelPersistence = HotelPersistenceService.getInstance(service, roomTypePersistence,
				roomPersistence, guestPersistence, bookingPersistence);

		int excessLevel = -1;
		int guestId = 0;

		HotelApplContext context = new HotelApplContext(inOut, service, analytics, roomTypePersistence, roomPersistence,
				guestPersistence, bookingPersistence, hotelPersistence, excessLevel, guestId);

		try {
			boolean filesExist = Files.exists(Path.of(HotelApplConstants.DIR, HotelApplConstants.ROOMTYPE_FILE))
					&& Files.exists(Path.of(HotelApplConstants.DIR, HotelApplConstants.ROOM_FILE))
					&& Files.exists(Path.of(HotelApplConstants.DIR, HotelApplConstants.GUEST_FILE))
					&& Files.exists(Path.of(HotelApplConstants.DIR, HotelApplConstants.BOOKING_FILE));
			if (filesExist) {
				context.getHotelPersistence().loadHotelData();
				System.out.println("- Hotel data is loaded from file");
			} else {
				System.out.println("- Data files not found");
				RandomHotelDataGenerator randomHotelData = new RandomHotelDataGenerator(context);
				randomHotelData.generate();
				System.out.println("- Hotel data is generated");
			}
			System.out.println();
			while (true) {
				Menu loginMenu = new Menu(getLoginMenuItems(context), inOut);
				loginMenu.runMenu();
				switch (context.getExcessLevel()) {
					case -1: {
						inOut.outputlLine("Service is closed");
						return;
					}
					case 0: {
						inOut.outputlLine("Welcome guest " + service.getGuests().get(context.getGuestId()).getName());
						Menu guestMenu = new Menu(getGuestMenuItems(context), inOut);
						guestMenu.runMenu();
						inOut.outputlLine("Service is closed");
						break;
					}
					case 1: {
						inOut.outputlLine("Welcome manager");
						Menu managerMenu = new Menu(getManagerMenu(context), inOut);
						managerMenu.runMenu();
						inOut.outputlLine("Service is closed");
						break;
					}
					case 2: {
						inOut.outputlLine("Welcome accountant");
						Menu accountantMenu = new Menu(getAccountantMenu(context), inOut);
						accountantMenu.runMenu();
						inOut.outputlLine("Service is closed");
						break;
					}
				}
				if(context.getExcessLevel() != -1) break;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("service terminated");
		}
	}

	private static Item[] getAccountantMenu(HotelApplContext context) {
		return new Item[] { new ShowAllStatisticsItem(context), new ShowBookingsByCheckInItem(context),
				new ShowMostPopularRoomForRangeAgeItem(context), new ShowOccupancyForDateItem(context),
				new LogOutItem(context), new ExitItem() };
	}

	private static Item[] getManagerMenu(HotelApplContext context) {
		return new Item[] { new AddRoomTypeItem(context), new AddRoomItem(context), new RemoveRoomTypeItem(context),
				new RemoveRoomItem(context), new RemoveGuestItem(context), new RemoveBookingItem(context),
				new ShowRoomTypesItem(context), new ShowRoomsItem(context), new ShowGuestsItem(context),
				new ShowBookingsItem(context), new LogOutItem(context), new SaveAndExitItem(context) };
	}

	private static Item[] getGuestMenuItems(HotelApplContext context) {
		return new Item[] { new CreateBookingItem(context), new CancelMyBookingItem(context),
				new ShowAvailableRoomsItem(context), new ShowMyBookingsItem(context), new LogOutItem(context), new SaveAndExitItem(context) };
	}

	private static Item[] getLoginMenuItems(HotelApplContext context) {
		return new Item[] { new LoginItem(context), new RegisterGuestItem(context), new SaveAndExitItem(context) };
	}
}
