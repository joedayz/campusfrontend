package pe.joedayz.campus.rest.curso;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pe.joedayz.campus.dto.TemasCursoDto;
import pe.joedayz.campus.rest.BackendRestInvoker;

@RestController
@RequestMapping("/cursoApi")
public class CursoApiController {

	
    @Value("${backend.server}")
    private String server;

    @Value("${backend.port}")
    private int port;

    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/temasDisponibles", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    List<TemasCursoDto> find(@RequestParam("keyCurso") String keyCurso
    ) {


		BackendRestInvoker restInvoker= new BackendRestInvoker<List<TemasCursoDto>>(server,port);

		ResponseEntity<List<TemasCursoDto>> responseEntity=
				restInvoker.sendGet("/curso/temasDisponibles?keyCurso="+keyCurso,List.class);


		return responseEntity.getBody();
    }

}
