package pe.joedayz.campus.dto;

import java.util.ArrayList;
import java.util.List;

public class CursoDto {

	private Long cursoId;
    private Long tipoCursoId;
    private String tituloCurso;
    private String tituloFooterCurso;
    private String status;
    private String destacadoHome;
    private List<TemasCursoDto> rolList = new ArrayList<>();
    
    
	public Long getCursoId() {
		return cursoId;
	}
	public void setCursoId(Long cursoId) {
		this.cursoId = cursoId;
	}
	public Long getTipoCursoId() {
		return tipoCursoId;
	}
	public void setTipoCursoId(Long tipoCursoId) {
		this.tipoCursoId = tipoCursoId;
	}
	public String getTituloCurso() {
		return tituloCurso;
	}
	public void setTituloCurso(String tituloCurso) {
		this.tituloCurso = tituloCurso;
	}
	public String getTituloFooterCurso() {
		return tituloFooterCurso;
	}
	public void setTituloFooterCurso(String tituloFooterCurso) {
		this.tituloFooterCurso = tituloFooterCurso;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDestacadoHome() {
		return destacadoHome;
	}
	public void setDestacadoHome(String destacadoHome) {
		this.destacadoHome = destacadoHome;
	}
	public List<TemasCursoDto> getRolList() {
		return rolList;
	}
	public void setRolList(List<TemasCursoDto> rolList) {
		this.rolList = rolList;
	}
   
 
    
}
