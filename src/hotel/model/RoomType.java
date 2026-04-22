package hotel.model;

import java.io.Serializable;
import java.util.Objects;

public class RoomType implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	private double pricePerNight;
	private int capacity;
	
	public RoomType(String name, double pricePerNight, int capacity) {
		this.name = name;
		this.pricePerNight = pricePerNight;
		this.capacity = capacity;
	}

	@Override
	public String toString() {
		return "RoomType [name=" + name + ", pricePerNight=" + pricePerNight + ", capacity=" + capacity + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(capacity, name, pricePerNight);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RoomType other)) 
            return false;
		return capacity == other.capacity && Objects.equals(name, other.name)
				&& Double.doubleToLongBits(pricePerNight) == Double.doubleToLongBits(other.pricePerNight);
	}

	public String getName() {
		return name;
	}

	public double getPricePerNight() {
		return pricePerNight;
	}

	public int getCapacity() {
		return capacity;
	}
}
