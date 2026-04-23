package hotel.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Guest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final int id;
	private String name;
	private String email;
	private LocalDate dateOfBirth; 
	private transient String password;
	
	public Guest(int id, String name, String email, LocalDate dateOfBirth, String password) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "Guest [id=" + id + ", name=" + name + ", email=" + email + ", date of birth=" + dateOfBirth + ", password=" + password + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Guest other)) 
            return false;
		return id == other.id;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}
	
	public LocalDate getDateOfBirth(){
		return dateOfBirth;
	}
	
	public String getPassword() {
		return password;
	}
}
