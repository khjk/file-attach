package flow.fileattach.domain.item;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "document")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "document_id")
    private Long id;

    private String description;

    @Column(updatable = false)
    private String savedTime;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private List<AttachFile> attachFiles;

    @PrePersist
    public void prePersist() {
        this.savedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }
}
