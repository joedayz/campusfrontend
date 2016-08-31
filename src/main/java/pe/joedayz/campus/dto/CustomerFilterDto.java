package pe.joedayz.campus.dto;

public class CustomerFilterDto extends PageableFilter {

	private String code;	
	private String name;
	private String status;
	private String isSearch;

	public String getIsSearch() {
		return isSearch;
	}

	public void setIsSearch(String isSearch) {
		this.isSearch = isSearch;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



}
