package pe.joedayz.campus.service.intf;

import java.util.List;

import pe.joedayz.campus.dto.GeneralTableDto;

public interface GeneralTableService {

    GeneralTableDto find(Long generalTableId);
    List<GeneralTableDto> findGeneralTableByGroup();
  
    
}
