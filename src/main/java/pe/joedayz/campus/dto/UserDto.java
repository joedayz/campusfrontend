package pe.joedayz.campus.dto;

import java.util.ArrayList;
import java.util.List;

import pe.joedayz.campus.enums.RoleEnum;

/**
 * Created by awusr on 04/05/2016.
 */
public class UserDto {

    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String status;


    private String title;
    private String email;
    private String confirmPassword;
    private String newPassword;
    
    List<RolDto> rolList = new ArrayList<>();
    List<RoleViewDto> roleList= new ArrayList<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public List<RolDto> getRolList() {
        return rolList;
    }

    public void setRolList(List<RolDto> rolList) {
        this.rolList = rolList;
    }

    public boolean hasRol(RoleEnum rol) {
        for (RolDto rolDto : rolList) {
            if (rol.getCode().equals(rolDto.getCode()))
                return true;
        }
        return false;
    }
    
        public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
        public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

   public List<RoleViewDto> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RoleViewDto> roleList) {
        this.roleList = roleList;
    }
    
    
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
