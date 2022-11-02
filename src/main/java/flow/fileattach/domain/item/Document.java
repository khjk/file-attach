package flow.fileattach.domain.item;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "document")
@Getter @Setter
@EntityListeners(AuditingEntityListener.class)
public class Document {
    @Id
    @GeneratedValue
    @Column(name = "document_id")
    private Long id;

    private String description;

    @CreatedDate
    private LocalDateTime savedTime;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private List<AttachFile> attachFiles;
}
