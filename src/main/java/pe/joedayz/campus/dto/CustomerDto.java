package pe.joedayz.campus.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acme on 27/04/2016.
 */
public class CustomerDto extends AuditDto {

	private Long customerId;
	private String code;
	private String customerName;
	private String status;

	private Integer rowVersion;

	private List<StatusProcessDto> statusProcessDtoList;

	public CustomerDto() {
		this.statusProcessDtoList = new ArrayList<>();
	}
	public Integer getRowVersion() {
		return rowVersion;
	}

	public void setRowVersion(Integer rowVersion) {
		this.rowVersion = rowVersion;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void addErrorStatus(String s) {

		StatusProcessDto statusProcessDto = new StatusProcessDto();
		statusProcessDto.setStatus("Error");
		statusProcessDto.setMessage(s);
		statusProcessDtoList.add(statusProcessDto);
	}

}
