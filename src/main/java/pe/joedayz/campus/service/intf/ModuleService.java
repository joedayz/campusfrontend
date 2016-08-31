package pe.joedayz.campus.service.intf;

import java.net.URISyntaxException;
import java.util.List;

import pe.joedayz.campus.dto.ModuleViewDto;

public interface ModuleService {
	
    ModuleViewDto findModule(Long moduleId) throws URISyntaxException;
    List<ModuleViewDto> searchModulesExceptItself(Long moduleId) throws URISyntaxException;
   
    
}

