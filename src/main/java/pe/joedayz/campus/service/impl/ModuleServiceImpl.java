package pe.joedayz.campus.service.impl;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import pe.joedayz.campus.dto.AutoCompleteModuleFilterDto;
import pe.joedayz.campus.dto.ModuleViewDto;
import pe.joedayz.campus.rest.BackendRestInvoker;
import pe.joedayz.campus.service.intf.ModuleService;

@Service
public class ModuleServiceImpl implements ModuleService {

	@Value("${backend.server}")
	private String server;

	@Value("${backend.port}")
	private int port;

	@Override
	public ModuleViewDto findModule(Long moduleId) throws URISyntaxException {

		BackendRestInvoker restInvoker = new BackendRestInvoker<ModuleViewDto>(server, port);

		ResponseEntity<ModuleViewDto> responseEntity = restInvoker.sendGet("/module/find/" + moduleId, ModuleViewDto.class);

		ModuleViewDto result = null;
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			result = responseEntity.getBody();

		}
		return result;
	}
	
	@Override
	public List<ModuleViewDto> searchModulesExceptItself(Long moduleId) throws URISyntaxException {

		ParameterizedTypeReference<List<ModuleViewDto>> typeRef=new ParameterizedTypeReference<List<ModuleViewDto>>(){};
		
		List<ModuleViewDto> modules= new ArrayList<ModuleViewDto>();
	    
		BackendRestInvoker restInvoker= new BackendRestInvoker<ModuleViewDto>(server,port);
	    AutoCompleteModuleFilterDto filter= new AutoCompleteModuleFilterDto();
	    filter.setQuery("");
	    filter.setModuleId(moduleId);
	    ResponseEntity<List<ModuleViewDto>> responseEntity = restInvoker.sendGet("/autocomplete/modulesExceptItself",filter,typeRef);
	    
	    if (responseEntity.getStatusCode() == HttpStatus.OK) {
	    	modules=responseEntity.getBody();
	    }
	    
	    return modules;
	}

}
