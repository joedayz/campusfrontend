package pe.joedayz.campus.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import pe.joedayz.campus.dto.ModuleFilterDto;
import pe.joedayz.campus.dto.ModuleViewDto;
import pe.joedayz.campus.dto.PageableResult;
import pe.joedayz.campus.service.impl.UserServiceImpl;
import pe.joedayz.campus.service.intf.ModuleService;
import pe.joedayz.campus.util.ModuleCodes;

@RestController
@RequestMapping("/module")
public class ModuleController {

	@Value("${backend.server}")
	private String server;

	@Value("${backend.port}")
	private int port;
	
	ModuleFilterDto prFilterGlobal;
	
	@Autowired
	ModuleService moduleService;
	
	@Autowired
    UserServiceImpl userServicePermission;
	
	@RequestMapping("/search")
	ModelAndView search() throws Exception {
		
		boolean readOnlyModule = userServicePermission.hasROAccess(ModuleCodes.MODULE_SEARCH);
		ModelAndView mav = new ModelAndView("module/search::partial-view");
		mav.addObject("readOnlyModule",readOnlyModule);
		return mav;
	}
	
	@RequestMapping(value = "/search/find", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	PageableResult find(ModuleFilterDto prFilter) {
		prFilterGlobal = prFilter;
		PageableResult pageableResult = new PageableResult();

		BackendRestInvoker restInvoker = new BackendRestInvoker<PageableResult>(server, port);

		ResponseEntity<PageableResult> responseEntity = restInvoker.sendPost("/module/searchModule/", prFilter, PageableResult.class);

		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			pageableResult = responseEntity.getBody();

		}
		return pageableResult;
	}
	
	@RequestMapping("/view")
	ModelAndView view(Long moduleId) throws Exception {
		
		boolean readOnlyModule = userServicePermission.hasROAccess(ModuleCodes.MODULE_VIEW);
		
		List<ModuleViewDto> modules = moduleService.searchModulesExceptItself(moduleId);
		ModuleViewDto result = moduleService.findModule(moduleId);
				
		ModelAndView mav = new ModelAndView("module/module::partial-view");
		mav.addObject("title", "Edit Module " + result.getCode());
		mav.addObject("titleButton", "Save");
		mav.addObject("moduleModel", result);
		mav.addObject("modules", modules);
		mav.addObject("readOnlyModule",readOnlyModule);

		return mav;
	}

	
}
