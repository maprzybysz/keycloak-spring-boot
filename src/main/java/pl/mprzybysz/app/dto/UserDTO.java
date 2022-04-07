package pl.mprzybysz.app.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserDTO {

	@NotBlank
	@Size(min = 4, max = 20)
	private String username;
	@NotBlank
	@Email
	private String email;
	@NotBlank
	private String password;
	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName;
	private boolean enabled = true;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "UserDTO [username=" + username + ", email=" + email + ", firstName=" + firstName + ", lastName="
				+ lastName + "]";
	}

}
