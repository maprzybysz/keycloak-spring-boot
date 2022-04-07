package pl.mprzybysz.app.mapper;

import org.mapstruct.Mapper;

import pl.mprzybysz.app.dto.AppUserDTO;
import pl.mprzybysz.app.entity.AppUser;

@Mapper(componentModel = "spring")
public interface AppUserMapper {

	AppUserDTO mapToAppUserDTO(AppUser appUser);

}
