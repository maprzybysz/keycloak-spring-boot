package pl.mprzybysz.app.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.mprzybysz.app.constant.ErrorConstant;
import pl.mprzybysz.app.dto.UserDTO;
import pl.mprzybysz.app.dto.UserLoginDTO;
import pl.mprzybysz.app.entity.AppUser;
import pl.mprzybysz.app.exception.AuthorizationException;
import pl.mprzybysz.app.exception.RegistrationException;
import pl.mprzybysz.app.exception.YourDrivingLicenseException;
import pl.mprzybysz.app.mapper.KeycloakUserMapper;

/**
 * @author mefiu
 *
 */
@Service
@Transactional
public class KeycloakService {

	private final Logger log = LoggerFactory.getLogger(KeycloakService.class);

	@Value("${keycloak.auth-server-url}")
	private String authServerUrl;
	@Value("${keycloak.realm}")
	private String realm;
	@Value("${keycloak.resource}")
	private String clientId;
	@Value("${keycloak.credentials.secret}")
	private String clientSecret;

	private KeycloakUserMapper keycloakUserMapper;
	private AppUserService appUserService;

	public KeycloakService(KeycloakUserMapper keycloakUserMapper, AppUserService appUserService) {
		this.keycloakUserMapper = keycloakUserMapper;
		this.appUserService = appUserService;
	}

	/**
	 * Get keycloak client configuration
	 * 
	 * @return Configuration
	 * @throws YourDrivingLicenseException
	 */
	public Configuration getConfiguration() throws YourDrivingLicenseException {
		log.info("getConfiguration()");

		try {
			Map<String, Object> clientCredentials = new HashMap<>();
			clientCredentials.put("secret", this.clientSecret);
			clientCredentials.put("grant_type", "password");

			return new Configuration(authServerUrl, realm, clientId, clientCredentials, null);
		} catch (Exception e) {
			log.error("getConfiguration() error: {}", e.getMessage());
			throw new YourDrivingLicenseException();
		}
	}

	/**
	 * Get keycloak authorization client
	 * 
	 * @return AuthzClient
	 * @throws YourDrivingLicenseException
	 */
	public AuthzClient getAuthzClient() throws YourDrivingLicenseException {
		log.info("getAuthzClient()");

		try {
			return AuthzClient.create(getConfiguration());
		} catch (Exception e) {
			log.error("getAuthzClient() error: {}", e.getMessage());
			throw new YourDrivingLicenseException();
		}
	}

	/**
	 * Get keycloak client
	 * 
	 * @return Keycloak
	 */
	public Keycloak getKeycloakClient() {
		return KeycloakBuilder.builder().serverUrl(authServerUrl).grantType(OAuth2Constants.CLIENT_CREDENTIALS)
				.realm(realm).clientId(clientId).clientSecret(clientSecret)
				.resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build();
	}

	/**
	 * Authorize keycloak user
	 * 
	 * @param loginDTO
	 * @return AccessTokenResponse
	 * @throws YourDrivingLicenseException
	 * @throws AuthorizationException
	 */
	public AccessTokenResponse authUser(UserLoginDTO loginDTO)
			throws YourDrivingLicenseException, AuthorizationException {
		log.info("authUser() userName: {}", loginDTO.getUsername());

		try {
			return getAuthzClient().obtainAccessToken(loginDTO.getUsername(), loginDTO.getPassword());
		} catch (Exception e) {
			log.error("authUser() error: {}", e.getMessage());
			throw new AuthorizationException(e.getMessage());
		}
	}

	/**
	 * Create new keycloak user
	 * 
	 * @param userDTO
	 * @throws RegistrationException
	 * @throws YourDrivingLicenseException
	 */
	public void createUser(UserDTO userDTO) throws RegistrationException, YourDrivingLicenseException {
		log.info("createUser() username: {}", userDTO.getUsername());

		try {
			Keycloak keycloak = getKeycloakClient();

			keycloak.tokenManager().getAccessToken();

			RealmResource realmResource = keycloak.realm(realm);
			UsersResource usersResource = realmResource.users();

			if (isUserExistByUsername(usersResource, userDTO.getUsername())) {
				throw new RegistrationException(ErrorConstant.USERNAME_ALREADY_EXISTS);
			}

			if (isUserExistByEmail(usersResource, userDTO.getEmail())) {
				throw new RegistrationException(ErrorConstant.EMAIL_ALREADY_EXISTS);
			}

			Response response = usersResource.create(keycloakUserMapper.mapToUserRepresentation(userDTO));

			if (response.getStatus() == 201) {
				String userId = CreatedResponseUtil.getCreatedId(response);

				log.info("Created userId: {}", userId);

				CredentialRepresentation password = new CredentialRepresentation();
				password.setTemporary(false);
				password.setType(CredentialRepresentation.PASSWORD);
				password.setValue(userDTO.getPassword());

				UserResource userResource = usersResource.get(userId);

				userResource.resetPassword(password);

				RoleRepresentation realmRoleUser = realmResource.roles().get("user").toRepresentation();

				userResource.roles().realmLevel().add(Arrays.asList(realmRoleUser));

				AppUser newUser = new AppUser(userId);
				appUserService.addAppUser(newUser);
			}
		} catch (RegistrationException e) {
			log.error("createUser() error: {}", e.getMessage());
			throw new RegistrationException(e.getMessage());
		} catch (Exception e) {
			log.error("createUser() error: {}", e.getMessage());
			throw new YourDrivingLicenseException();
		}
	}

	/**
	 * Check if user exists by userName
	 * 
	 * @param usersResource
	 * @param userName
	 * @return boolean
	 */
	private boolean isUserExistByUsername(UsersResource usersResource, String userName) {
		return !usersResource.search(userName, true).isEmpty();
	}

	/**
	 * Check if user exists by email
	 * 
	 * @param usersResource
	 * @param email
	 * @return boolean
	 */
	private boolean isUserExistByEmail(UsersResource usersResource, String email) {
		return !usersResource.searchByAttributes("email:" + email).isEmpty();
	}
}
