package flow.fileattach.web.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class FileForm {
    private Long fileId;
    private String description;
    private List<MultipartFile> attachFiles;
}
