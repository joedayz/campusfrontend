package pe.joedayz.campus.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pe.joedayz.campus.dto.GeneralTableDto;
import pe.joedayz.campus.dto.GeneralTableFilterDto;
import pe.joedayz.campus.dto.PageableResult;
import pe.joedayz.campus.service.impl.UserServiceImpl;
import pe.joedayz.campus.service.intf.GeneralTableService;
import pe.joedayz.campus.util.ModuleCodes;

import java.util.List;

/**
 * Created by josediaz on 9/2/16.
 */
@RestController
@RequestMapping("/generalTable")
public class GeneralTableController {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Value("${backend.server}")
    private String server;

    @Value("${backend.port}")
    private int port;

    @Autowired
    GeneralTableService generalTableService;

    @Autowired
    UserServiceImpl userServicePermission;

    @RequestMapping("/search")
    ModelAndView search() throws Exception {

        boolean readOnlyModule = userServicePermission.hasROAccess(ModuleCodes.CODE_SEARCH);
        List<GeneralTableDto> generalTables = generalTableService.findGeneralTableByGroup();
        ModelAndView mav = new ModelAndView("generaltable/search::partial-view");
        mav.addObject("generalTables", generalTables);
        mav.addObject("readOnlyModule",readOnlyModule);
        return mav;
    }

    @RequestMapping("/view")
    ModelAndView view(Long generalTableId) throws JsonProcessingException {

        boolean readOnlyModule = userServicePermission.hasROAccess(ModuleCodes.CODE_VIEW);
        List<GeneralTableDto> generalTables = generalTableService.findGeneralTableByGroup();
        ModelAndView mav = new ModelAndView("generaltable/general-table::partial-view");
        GeneralTableDto result = generalTableService.find(generalTableId);
        mav.addObject("title", "Edit Code " + result.getCode());
        mav.addObject("titleButton", "Save");
        mav.addObject("generalTableModel", result);
        mav.addObject("generalTables", generalTables);
        mav.addObject("readOnlyModule",readOnlyModule);
        return mav;
    }

    @RequestMapping("/new")
    ModelAndView newGeneralTable() throws JsonProcessingException {
        List<GeneralTableDto> generalTables = generalTableService.findGeneralTableByGroup();
        ModelAndView mav = new ModelAndView("generaltable/general-table::partial-view");
        GeneralTableDto generalTableDto = new GeneralTableDto();
        String serialization = OBJECT_MAPPER.writeValueAsString(generalTableDto);
        mav.addObject("title", "New Codes");
        mav.addObject("titleButton", "Save");
        mav.addObject("generalTableModel", serialization);
        mav.addObject("generalTables", generalTables);

        return mav;
    }

    @RequestMapping(value = "/search/find", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    PageableResult find(GeneralTableFilterDto prFilter) {

        PageableResult pageableResult = new PageableResult();
        BackendRestInvoker restInvoker = new BackendRestInvoker<PageableResult>(server, port);

        ResponseEntity<PageableResult> responseEntity = restInvoker.sendPost("/generalTable/find/", prFilter, PageableResult.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            pageableResult = responseEntity.getBody();
        }
        return pageableResult;
    }



}