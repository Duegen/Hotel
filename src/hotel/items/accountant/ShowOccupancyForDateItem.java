package hotel.items.accountant;

import java.time.LocalDate;

import hotel.HotelApplConstants;
import hotel.HotelApplContext;
import hotel.items.HotelItem;

public class ShowOccupancyForDateItem extends HotelItem {

	public ShowOccupancyForDateItem(HotelApplContext context) {
		super(context);
		
	}

	@Override
	public String displayName() {
		
		return "Show occupancy for date";
	}

	@Override
	public void perform() {
		LocalDate selected = inOut.inputDate("Enter date in format", HotelApplConstants.DATE_FORMAT);
		if(selected == null)
			return;
		inOut.outputlLine("Occupied rooms for : "+ selected + ": "+
				analyticsService
				.getOccupiedRoomsCount(selected));
	}

}
