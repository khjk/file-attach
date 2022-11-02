package flow.fileattach.domain.repository;

import flow.fileattach.domain.item.AttachFile;
import flow.fileattach.domain.item.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FileRepository {

    private final EntityManager em;

    public void save(Document post) {
        if (post.getId() == null) {
            em.persist(post);
        } else {
            em.merge(post);
        }
    }

    public Document findDocument(Long documentId) {
        return em.find(Document.class, documentId);
    }

    public List<Document> findAllDocuments() {
        return em.createQuery("select d from Document d", Document.class)
                .getResultList();
    }

    public AttachFile findFile(Long fileId) {
        return em.find(AttachFile.class, fileId);
    }

}
