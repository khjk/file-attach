package flow.fileattach.domain.repository;

import flow.fileattach.domain.item.Ext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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
        Ext ext = findById(extName);
        em.remove(ext);
    }

    public List<Ext> findExtByCondition(ExtSearch extSearch) {

        String jpql = "select e from Ext e Where 1=1";

        if (extSearch.getIsForbidden() != null) {
            jpql += " and e.checkedYn = :checkedYn";
        }
        if (extSearch.getIsFixed() != null) {
            jpql += " and e.fixedYn = :fixedYn";
        }
        TypedQuery<Ext> query = em.createQuery(jpql, Ext.class);

        if (extSearch.getIsForbidden() != null) {
            query.setParameter("checkedYn", extSearch.getIsForbidden());
        }
        if (extSearch.getIsFixed() != null) {
            query.setParameter("fixedYn", extSearch.getIsFixed());
        }

        return query.getResultList();
    }


}
