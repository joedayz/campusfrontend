package pe.joedayz.campus.dto;

public class AutoCompleteModuleFilterDto {

    private Long moduleId;
    private String query;

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
    
}
