package pl.mprzybysz.app.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
public class AppUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	@Column(name = "KEYCLOAK_ID", nullable = false)
	private String keycloakId;

	public AppUser() {
	}

	public AppUser(String keycloakId) {
		this.keycloakId = keycloakId;
	}

	public AppUser(Long id, String keycloakId) {
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
	public int hashCode() {
		return Objects.hash(id, keycloakId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppUser other = (AppUser) obj;
		return Objects.equals(id, other.id) && Objects.equals(keycloakId, other.keycloakId);
	}

	@Override
	public String toString() {
		return "AppUser [id=" + id + ", keycloakId=" + keycloakId + "]";
	}

}
