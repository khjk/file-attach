package flow.fileattach.domain.repository;

import flow.fileattach.domain.item.Document;
import flow.fileattach.domain.item.Ext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ExtRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(Ext ext) {
        em.persist(ext);
    }

    public Ext findById(String extName) {
        return em.find(Ext.class, extName);
    }

    public void delete(String extName) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        Ext ext = findById(extName);
        em.remove(ext);

        transaction.commit();
    }

    public List<Ext> findExtByCondition(Boolean isForbidden) {
        String checkedYn = isForbidden == true ? "Y" : "N";

        String jpql = "select e from Ext e where e.checkedYn = :checkedYn";
        TypedQuery<Ext> query = em.createQuery(jpql, Ext.class);

        return query.setParameter("checkedYn", checkedYn).getResultList();
    }


}
