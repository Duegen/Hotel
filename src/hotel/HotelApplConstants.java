package hotel;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class HotelApplConstants {
	private final static Properties props;
	public final static String DIR = init("DIR", "Data");
	public final static String ROOM_FILE = init("ROOM_FILE", "rooms.dat");
	public final static String ROOMTYPE_FILE = init("ROOMTYPE_FILE", "roomtypes.dat");
	public final static String GUEST_FILE = init("GUEST_FILE", "guests.dat");
	public final static String BOOKING_FILE = init("BOOKING_FILE", "bookings.dat");
	public final static String DATE_FORMAT = init("DATE_FORMAT", "dd.MM.YYYY");
	public static final String ALGORITHM = init("ALGORITHM", "PBKDF2WithHmacSHA1");
    public static final int KEY_LENGTH = init("KEY_LENGTH", 256); 
	
	static {
		Properties propsTmp = new Properties();
		try (FileInputStream env = new FileInputStream("hotel.env")) {
		    propsTmp.load(env);
		}catch (IOException e) {
			propsTmp = null;
		}
		props = propsTmp;
	}

	private static String init(String prop, String defaultValue) {
		String propTmp;
		if(Objects.isNull(props))
			return defaultValue;
		else {
			propTmp = props.getProperty(prop);
			return propTmp == null ? defaultValue : propTmp;
		}
	}

	private static int init(String prop, int defaultValue) {
		Integer propTmp;
		if(Objects.isNull(props))
			return defaultValue;
		else {
			try {
				propTmp = Integer.valueOf(props.getProperty(prop));
			} catch (Exception e) {
				return defaultValue;
			}
			return propTmp;
		}
	}
}
