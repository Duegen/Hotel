package hotel.generator;

import java.util.HashMap;
import java.util.Map;

public final class Constants {
	public static final String[] NAMES = {"Standard", "Deluxe", "Suite"};
	public static final int[] CAPACITY = {1,2,4,8};
	public static final double[] PRICE_PER_PERSON_PER_NIGHT = {350.0, 500.0, 670.0};
	
	public final static int[] ROOM_NUMBERS = 
		{2 , 7, 12, 17, 23, 28, 33, 36, 41, 45, 57, 
		58, 67, 69, 72, 78, 83, 88, 90, 99};
	
	public final static int GUEST_MIN_ID = 1;
	public final static int GUEST_MAX_ID = 100;
	public final static Map<String, String> NAMES_EMAILS = getNamesEmails();
	
	public final static int BOOKING_MIN_ID = 1;
	public final static int BOOKING_MAX_ID = 100_000;
	public static final int ROOM_MIN_NUMBER = 0;
	public static final int ROOM_MAX_NUMBER = 100;
	public static final int ROOM_MIN_CAP = 1;
	public static final int ROOM_MAX_CAP = 10;
	public static final double PRICE_PER_NIGHT_MIN = 100.0;
	public static final double PRICE_PER_NIGHT_MAX = 10000.0;
	
	
	private static Map<String, String> getNamesEmails(){
		Map<String, String> result = new HashMap<String, String>();
		result.put("Alice", "alice@example.com");
		result.put("Benjamin", "benjamin@example.com");
		result.put("Charlotte", "charlotte@example.com");
		result.put("Daniel", "daniel@example.com");
		result.put("Eleanor", "eleanor@example.com");
		result.put("Felix", "felix@example.com");
		result.put("Grace", "grace@example.com");
		result.put("Henry", "henry@example.com");
		result.put("Isabella", "isabella@example.com");
		result.put("Julian", "julian@example.com");
		return result;
	}
}
