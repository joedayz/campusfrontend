package pe.joedayz.campus.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import pe.joedayz.campus.dto.AutoCompleteFilterDto;
import pe.joedayz.campus.dto.PageableResult;
import pe.joedayz.campus.dto.ParameterDto;
import pe.joedayz.campus.dto.ParameterFilterDto;
import pe.joedayz.campus.service.impl.UserServiceImpl;
import pe.joedayz.campus.service.intf.ParameterService;
import pe.joedayz.campus.util.ModuleCodes;

@RestController
@RequestMapping("/parameter")
public class ParameterController {

	@Value("${backend.server}")
	private String server;

	@Value("${backend.port}")
	private int port;
	
	@Autowired
	ParameterService parameterService;
	
	@Autowired
    UserServiceImpl userServicePermission;
	
	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	
	@RequestMapping("/search")
	ModelAndView search() throws Exception {

		boolean readOnlyModule = userServicePermission.hasROAccess(ModuleCodes.APPLICATION_SETTING_SEARCH);
		ModelAndView mav = new ModelAndView("parameter/search::partial-view");
		mav.addObject("readOnlyModule",readOnlyModule);
		return mav;
	}
	
	@RequestMapping(value="/find/autocomplete", method=RequestMethod.GET, produces="application/json")
	public List<ParameterDto> findParameters(@RequestParam("query")  String query) {


		BackendRestInvoker restInvoker= new BackendRestInvoker<ParameterDto>(server,port);

		AutoCompleteFilterDto filter= new AutoCompleteFilterDto();
		filter.setQuery(query);
		ResponseEntity<List<ParameterDto>> responseEntity = restInvoker.sendGetList("/autocomplete/parameter",filter,ParameterDto.class);

		return responseEntity.getBody();
	}
	
	@RequestMapping("/view")
	ModelAndView view(Long parameterId) throws Exception {

		boolean readOnlyModule = userServicePermission.hasROAccess(ModuleCodes.APPLICATION_SETTING_VIEW);
		ParameterDto result = parameterService.findParameter(parameterId);

		ModelAndView mav = new ModelAndView("parameter/parameter::partial-view");
		mav.addObject("title", "Edit Parameter " + result.getCode());
		mav.addObject("titleButton", "Save");
		mav.addObject("parameterModel", result);
		mav.addObject("readOnlyModule",readOnlyModule);

		return mav;
	}

	@RequestMapping(value = "/search/find", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	PageableResult find(ParameterFilterDto prFilter) {

		PageableResult pageableResult = new PageableResult();

		BackendRestInvoker restInvoker = new BackendRestInvoker<PageableResult>(server, port);

		ResponseEntity<PageableResult> responseEntity = restInvoker.sendPost("/parameter/searchParameter/", prFilter, PageableResult.class);

		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			pageableResult = responseEntity.getBody();
		}

		return pageableResult;
	}

}
