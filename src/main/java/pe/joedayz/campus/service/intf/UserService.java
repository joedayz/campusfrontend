package pe.joedayz.campus.service.intf;

import java.util.List;

import pe.joedayz.campus.dto.UserDto;
import pe.joedayz.campus.enums.RoleEnum;

public interface UserService {

     //boolean validateUserRol(RoleEnum roleEnum);

     boolean hasAny(List<RoleEnum> roles);

     boolean hasAny(RoleEnum role);

     UserDto getUserSession();
}
