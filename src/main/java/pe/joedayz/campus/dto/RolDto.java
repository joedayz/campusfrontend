package pe.joedayz.campus.dto;

/**
 * Created by MATRIX-JAVA on 5/5/2016.
 */
public class RolDto {

    private Long id;

    private String code;

    public RolDto() {
    }

    public RolDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
