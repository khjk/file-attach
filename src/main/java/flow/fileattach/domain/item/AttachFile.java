package flow.fileattach.domain.item;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "file")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class AttachFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="document_id", nullable = false)
    private Document document;

    @NotNull
    private String uploadFileName; //고객이 업로드한 파일명

    @NotNull
    private String storeFileName; //서버 내부에서 관리하는 파일명

    @NotNull
    private String ext; //확장자명

    public AttachFile(Document document, String uploadFileName, String storeFileName, String ext) {
        this.document = document;
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.ext = ext;
    }

}
