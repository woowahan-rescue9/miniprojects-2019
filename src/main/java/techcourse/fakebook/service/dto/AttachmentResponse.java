package techcourse.fakebook.service.dto;

public class AttachmentResponse {
    private String name;
    private String path;

    private AttachmentResponse() {
    }

    public AttachmentResponse(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
