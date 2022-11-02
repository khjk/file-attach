package flow.fileattach;

import flow.fileattach.domain.item.Ext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDB {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        private final String[] fixedExt = {"bat", "cmd", "com", "cpl", "exe", "scr", "js"};
        public void dbInit() {
            Ext extVO = null;
            for(String extName : fixedExt) {
                extVO = createExt(extName);
                em.persist(extVO);
            }
        }

        private Ext createExt(String extName) {
            Ext extVO = new Ext();
            extVO.setExtName(extName);
            extVO.setFixedYn(true); //삭제불가
            extVO.setCheckedYn(false);
            return extVO;
        }
    }
}
