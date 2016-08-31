package pe.joedayz.campus.dto;

import java.util.List;

public class RoleViewDto extends AuditDto {

	private Long roleId;
	private String roleCode;
	private String roleName;
	private String roleStatus;
	private String isDeleteRole;
	private String isUpdateRole;
	private String isEditCode;
	private String isEditForceReadOnly;

	private String roleAccess;
	private List<ModuleViewDto> modules;

	public List<ModuleViewDto> getModules() {
		return modules;
	}

	public void setModules(List<ModuleViewDto> modules) {
		this.modules = modules;
	}


	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleStatus() {
		return roleStatus;
	}

	public void setRoleStatus(String roleStatus) {
		this.roleStatus = roleStatus;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleAccess() {
		return roleAccess;
	}

	public void setRoleAccess(String roleAccess) {
		this.roleAccess = roleAccess;
	}

	public String getIsDeleteRole() {
		return isDeleteRole;
	}

	public void setIsDeleteRole(String isDeleteRole) {
		this.isDeleteRole = isDeleteRole;
	}

	public String getIsUpdateRole() {
		return isUpdateRole;
	}

	public void setIsUpdateRole(String isUpdateRole) {
		this.isUpdateRole = isUpdateRole;
	}

	public String getIsEditCode() {
		return isEditCode;
	}

	public void setIsEditCode(String isEditCode) {
		this.isEditCode = isEditCode;
	}

	public String getIsEditForceReadOnly() {
		return isEditForceReadOnly;
	}

	public void setIsEditForceReadOnly(String isEditForceReadOnly) {
		this.isEditForceReadOnly = isEditForceReadOnly;
	}
}
