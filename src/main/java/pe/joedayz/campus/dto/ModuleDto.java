package pe.joedayz.campus.dto;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;


public class ModuleDto {
	
    private String name;
    private String url;
    private String code;
    private String permissionType;
    private String visible;

    private List<ModuleDto> subModules = new ArrayList<>();

    public ModuleDto() {
    }

    public ModuleDto(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ModuleDto> getSubModules() {
        return subModules;
    }

    public void setSubModules(List<ModuleDto> subModules) {
        this.subModules = subModules;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return "ModuleDto{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", code='" + code + '\'' +
                ", permissionType='" + permissionType + '\'' +
                ", subModules=" + (subModules == null ? "[]" : Joiner.on(",").join(subModules)) +
                '}';
    }
    
}
