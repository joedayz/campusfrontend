package pe.joedayz.campus.dto;

public class RoleFilterDto  extends  PageableFilter{
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
