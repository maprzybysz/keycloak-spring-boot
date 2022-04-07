package pl.mprzybysz.app.controller;

import java.net.URISyntaxException;
import java.security.Principal;

import javax.validation.Valid;

import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.mprzybysz.app.dto.AppUserDTO;
import pl.mprzybysz.app.dto.UserDTO;
import pl.mprzybysz.app.dto.UserLoginDTO;
import pl.mprzybysz.app.exception.AppUserException;
import pl.mprzybysz.app.exception.AuthorizationException;
import pl.mprzybysz.app.exception.RegistrationException;
import pl.mprzybysz.app.exception.YourDrivingLicenseException;
import pl.mprzybysz.app.exception.error.YourDrivingLicenseAuthorizationError;
import pl.mprzybysz.app.exception.error.YourDrivingLicenseRequestError;
import pl.mprzybysz.app.exception.error.YourDrivingLicenseServerError;
import pl.mprzybysz.app.service.AppUserService;
import pl.mprzybysz.app.service.KeycloakService;

@RequestMapping(value = "/users")
@RestController
public class UserController {

	private final Logger log = LoggerFactory.getLogger(UserController.class);

	private KeycloakService keycloakService;
	private AppUserService appUserService;

	public UserController(KeycloakService keycloakService, AppUserService appUserService) {
		this.keycloakService = keycloakService;
		this.appUserService = appUserService;
	}

	@PostMapping("/signin")
	public ResponseEntity<AccessTokenResponse> signin(@RequestBody @Valid UserLoginDTO loginDTO)
			throws URISyntaxException, YourDrivingLicenseException {
		log.info("REST request to signin username: {}", loginDTO.getUsername());

		try {
			return ResponseEntity.ok(keycloakService.authUser(loginDTO));
		} catch (AuthorizationException e) {
			throw new YourDrivingLicenseAuthorizationError("users/signin", e.getMessage());
		} catch (Exception e) {
			throw new YourDrivingLicenseServerError("users/signin", e.getMessage());
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<Void> signup(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
		log.info("REST request to signup username: {}", userDTO.getUsername());

		try {
			keycloakService.createUser(userDTO);
			return ResponseEntity.ok().build();
		} catch (RegistrationException e) {
			throw new YourDrivingLicenseRequestError("/users/signup", e.getMessage());
		} catch (YourDrivingLicenseException e) {
			throw new YourDrivingLicenseServerError("/users/signup", e.getMessage());
		}
	}

	@GetMapping("/appUser")
	public ResponseEntity<AppUserDTO> getLoggedAppUser(Principal principal) throws URISyntaxException {
		log.info("REST request to getLoggedAppUser loggedKeycloakUserId: {}", principal.getName());

		try {
			return ResponseEntity.ok().body(appUserService.getAppUserByKeycloakId(principal.getName()));
		} catch (AppUserException e) {
			throw new YourDrivingLicenseRequestError("/users/user", e.getMessage());
		} catch (YourDrivingLicenseException e) {
			throw new YourDrivingLicenseServerError("/users/user", e.getMessage());
		}
	}

	@GetMapping("/user/{keycloakId}")
	public ResponseEntity<AppUserDTO> getAppUserByKeycloakId(@PathVariable String keycloakId)
			throws URISyntaxException {
		log.info("REST request to getAppUserByKeycloakId keycloakId: {}", keycloakId);

		try {
			return ResponseEntity.ok().body(appUserService.getAppUserByKeycloakId(keycloakId));
		} catch (AppUserException e) {
			throw new YourDrivingLicenseRequestError("/users/user", e.getMessage());
		} catch (YourDrivingLicenseException e) {
			throw new YourDrivingLicenseServerError("/users/user", e.getMessage());
		}
	}

}
