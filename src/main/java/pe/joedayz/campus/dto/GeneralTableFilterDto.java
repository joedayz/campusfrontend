package pe.joedayz.campus.dto;

public class GeneralTableFilterDto extends PageableFilter {

	private String tableName;
	private String code;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
