package pe.joedayz.campus.dto;

/**
 * Created by acme on 15/05/2016.
 */
public class RecoverPasswordDto {
    private String newPassword;
    private String confirmPassword;
    private String secToken;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getSecToken() {
        return secToken;
    }

    public void setSecToken(String secToken) {
        this.secToken = secToken;
    }
}
