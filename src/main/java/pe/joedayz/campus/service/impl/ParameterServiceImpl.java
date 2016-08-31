package pe.joedayz.campus.service.impl;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import pe.joedayz.campus.dto.ParameterDto;
import pe.joedayz.campus.rest.BackendRestInvoker;
import pe.joedayz.campus.service.intf.ParameterService;

@Service
public class ParameterServiceImpl implements ParameterService {

	@Value("${backend.server}")
	private String server;

	@Value("${backend.port}")
	private int port;

	@Override
	public ParameterDto findParameter(Long parameterId) throws URISyntaxException {

		BackendRestInvoker restInvoker = new BackendRestInvoker<ParameterDto>(server, port);

		ResponseEntity<ParameterDto> responseEntity = restInvoker.sendGet("/parameter/find/" + parameterId, ParameterDto.class);

		ParameterDto result = null;
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			result = responseEntity.getBody();

		}
		return result;
	}

}
