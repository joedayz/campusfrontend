package pe.joedayz.campus.service.intf;

import java.util.List;

import pe.joedayz.campus.dto.ModuleViewDto;
import pe.joedayz.campus.dto.RoleViewDto;

public interface RoleService {

    List<RoleViewDto> findAllRoles();
    List<ModuleViewDto> findMoludesActive();
    RoleViewDto find(Long roleId);

}