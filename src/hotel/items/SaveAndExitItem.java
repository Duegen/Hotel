package hotel.items;

import hotel.HotelApplContext;

public class SaveAndExitItem extends HotelItem{
	
	public SaveAndExitItem(HotelApplContext context) {
		super(context);
	
	}

	@Override
	public String displayName() {
		return "Save and exit";
	}

	@Override
	public void perform() {
		try {
			context.getHotelPersistence().saveHotelData();
			inOut.outputlLine("Data saved successfully");
		} catch (Exception e) {
			
			throw new IllegalStateException("Failed to save data: "+ e.getMessage());
		}
		
	}


	@Override
	public boolean isExit() {
		return true;
	}

	
}
