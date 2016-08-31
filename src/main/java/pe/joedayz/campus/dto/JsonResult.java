package pe.joedayz.campus.dto;

public class JsonResult {
    private String status="OK";
    private String description="";
    private int created=0;
    private int updated=0;
    private int deleted=0;
    private int error=0;
    private int processed=0;
    
    private String fileDocName="";

    public JsonResult(String status, String description) {
        this.status = status;
        this.description = description;
    }

    public JsonResult() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getProcessed() {
        return processed;
    }

    public void setProcessed(int processed) {
        this.processed = processed;
    }

    public String getFileDocName() {
        return fileDocName;
    }

    public void setFileDocName(String fileDocName) {
        this.fileDocName = fileDocName;
    }
}
