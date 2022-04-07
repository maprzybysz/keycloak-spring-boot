package pl.mprzybysz.app.dto;

import java.io.Serializable;

public class AppUserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String keycloakId;

	public AppUserDTO() {
	}

	public AppUserDTO(Long id, String keycloakId) {
		this.id = id;
		this.keycloakId = keycloakId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKeycloakId() {
		return keycloakId;
	}

	public void setKeycloakId(String keycloakId) {
		this.keycloakId = keycloakId;
	}

	@Override
	public String toString() {
		return "AppUserDTO [id=" + id + ", keycloakId=" + keycloakId + "]";
	}

}
