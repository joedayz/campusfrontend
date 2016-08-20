package pe.joedayz.campus.dto;

/**
 * Created by acme on 27/04/2016.
 */
public class CustomerDto {

    private Long customerId;
    private String customerName;
    private Long officeId;
    private String officeName;
    private Long salesAccountManagerId;
    private String salesAccountManagerName;

    public CustomerDto() {
    }

    public CustomerDto(String id, String code, String customerName) {
        this.customerName = customerName;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Long officeId) {
        this.officeId = officeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public Long getSalesAccountManagerId() {
        return salesAccountManagerId;
    }

    public void setSalesAccountManagerId(Long salesAccountManagerId) {
        this.salesAccountManagerId = salesAccountManagerId;
    }

    public String getSalesAccountManagerName() {
        return salesAccountManagerName;
    }

    public void setSalesAccountManagerName(String salesAccountManagerName) {
        this.salesAccountManagerName = salesAccountManagerName;
    }
}
