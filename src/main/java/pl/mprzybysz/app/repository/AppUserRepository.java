package pl.mprzybysz.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pl.mprzybysz.app.entity.AppUser;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

	@Query(value = "SELECT u FROM AppUser u WHERE u.keycloakId = :keycloakId")
	Optional<AppUser> findByKeycloakId(@Param("keycloakId") String keycloakId);

}
