package pe.joedayz.campus.service.intf;

import java.net.URISyntaxException;

import pe.joedayz.campus.dto.ParameterDto;

public interface ParameterService {
	
    ParameterDto findParameter(Long parameterId) throws URISyntaxException;
    
}
