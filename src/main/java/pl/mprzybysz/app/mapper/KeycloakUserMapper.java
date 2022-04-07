package pl.mprzybysz.app.mapper;

import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;

import pl.mprzybysz.app.dto.UserDTO;

@Mapper(componentModel = "spring")
public interface KeycloakUserMapper {

	UserDTO mapToUserDTO(UserRepresentation userRepresentation);

	UserRepresentation mapToUserRepresentation(UserDTO userDTO);

}
