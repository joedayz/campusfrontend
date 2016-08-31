package pe.joedayz.campus.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import pe.joedayz.campus.dto.GeneralTableDto;
import pe.joedayz.campus.rest.BackendRestInvoker;
import pe.joedayz.campus.service.intf.GeneralTableService;

@Service
public class GeneralTableServiceImpl implements GeneralTableService {
	@Value("${backend.server}")
	private String server;

	@Value("${backend.port}")
	private int port;

	@Override
	public GeneralTableDto find(Long generalTableId) {

		BackendRestInvoker restInvoker = new BackendRestInvoker<GeneralTableDto>(server, port);

		ResponseEntity<GeneralTableDto> responseEntity = restInvoker.sendGet("/generalTable/find/" + generalTableId,GeneralTableDto.class);

		GeneralTableDto result = null;
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			result = responseEntity.getBody();

		}

		return result;
	}

	@Override
	public List<GeneralTableDto> findGeneralTableByGroup() {
		ParameterizedTypeReference<List<GeneralTableDto>> typeRef=new ParameterizedTypeReference<List<GeneralTableDto>>(){};
		
		List<GeneralTableDto> tablesGroup= new ArrayList<GeneralTableDto>();
	    
		BackendRestInvoker restInvoker= new BackendRestInvoker<GeneralTableDto>(server,port);
	    
		ResponseEntity<List<GeneralTableDto>> responseEntity = restInvoker.sendGet("/autocomplete/findGeneralTableByGroup",typeRef);
	    
	    if (responseEntity.getStatusCode() == HttpStatus.OK) {
	    	tablesGroup=responseEntity.getBody();
	    }
	    return tablesGroup;
	}	
}