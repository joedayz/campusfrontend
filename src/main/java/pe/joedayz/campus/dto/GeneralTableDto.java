package pe.joedayz.campus.dto;

import java.math.BigDecimal;
import java.util.Date;

public class GeneralTableDto {

	private Long generalTableId;
	private String tableName;
	private String code;
	private String value;
	private BigDecimal sortOrder;
	private String status;
	private String createdBy;
	private Date createdDate;
	private String updatedBy;
	private Date updatedDate;
	private Integer rowVersion;
	
	public Integer getRowVersion() {
		return rowVersion;
	}

	public void setRowVersion(Integer rowVersion) {
		this.rowVersion = rowVersion;
	}

	public Long getGeneralTableId() {
		return generalTableId;
	}

	public void setGeneralTableId(Long generalTableId) {
		this.generalTableId = generalTableId;
	}

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public BigDecimal getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(BigDecimal sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
}
