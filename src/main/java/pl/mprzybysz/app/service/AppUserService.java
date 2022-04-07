package pl.mprzybysz.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.mprzybysz.app.constant.ErrorConstant;
import pl.mprzybysz.app.dto.AppUserDTO;
import pl.mprzybysz.app.entity.AppUser;
import pl.mprzybysz.app.exception.AppUserException;
import pl.mprzybysz.app.exception.YourDrivingLicenseException;
import pl.mprzybysz.app.mapper.AppUserMapper;
import pl.mprzybysz.app.repository.AppUserRepository;

@Service
@Transactional
public class AppUserService {

	private final Logger log = LoggerFactory.getLogger(AppUserService.class);

	private AppUserRepository appUserRepository;
	private AppUserMapper appUserMapper;

	public AppUserService(AppUserRepository appUserRepository, AppUserMapper appUserMapper) {
		this.appUserRepository = appUserRepository;
		this.appUserMapper = appUserMapper;
	}

	/**
	 * Fetch AppUser by keycloakId
	 * 
	 * @param String keycloakId
	 * @return AppUserDTO
	 * @throws AppUserException
	 * @throws YourDrivingLicenseException
	 */
	@Transactional(readOnly = true)
	public AppUserDTO getAppUserByKeycloakId(String keycloakId) throws AppUserException, YourDrivingLicenseException {
		log.info("getAppUserByKeycloakId() keycloakId: {}", keycloakId);

		try {
			AppUser appUser = appUserRepository.findByKeycloakId(keycloakId)
					.orElseThrow(() -> new AppUserException(ErrorConstant.APP_USER_NOT_FOUND));

			return appUserMapper.mapToAppUserDTO(appUser);
		} catch (AppUserException e) {
			log.error("getAppUserByKeycloakId() error: {}", e.getMessage());
			throw new AppUserException(e.getMessage());
		} catch (Exception e) {
			log.error("getAppUserByKeycloakId() error: {}", e.getMessage());
			throw new YourDrivingLicenseException();
		}
	}

	/**
	 * Add new AppUser
	 * 
	 * @param appUser
	 * @throws YourDrivingLicenseException
	 */
	public void addAppUser(AppUser appUser) throws YourDrivingLicenseException {
		log.info("addAppUser() appUser: {}", appUser);

		try {
			appUserRepository.save(appUser);
		} catch (Exception e) {
			log.error("addAppUser() error: {}", e.getMessage());
			throw new YourDrivingLicenseException();
		}
	}

}
