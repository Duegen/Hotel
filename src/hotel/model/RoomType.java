package hotel.model;

import java.io.Serializable;
import java.util.Objects;

public class RoomType implements Serializable{
	private static final long serialVersionUID = 1L;
	private int typeId;
	private String name;
	private double pricePerNight;
	private int capacity;
	
	public RoomType(int typeId, String name, double pricePerNight, int capacity) {
		this.typeId = typeId;
		this.name = name;
		this.pricePerNight = pricePerNight;
		this.capacity = capacity;
	}

	@Override
	public String toString() {
		return "RoomType [typeId=" + typeId + ", name=" + name + ", pricePerNight=" + pricePerNight + ", capacity=" + capacity + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(typeId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RoomType other)) 
            return false;
		return typeId == other.typeId;
	}
	
	public boolean isSame(RoomType other) {
		if(Objects.isNull(other))
			return false;
		return capacity == other.capacity
                && Double.doubleToLongBits(pricePerNight) == Double.doubleToLongBits(other.pricePerNight)
                && Objects.equals(name, other.name);
	}
	
	public int getTypeId() {
		return typeId;
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

	public Object toString2() {
		return "RoomType [name=" + name + ", pricePerNight=" + pricePerNight + ", capacity=" + capacity + "]";
	}
}
