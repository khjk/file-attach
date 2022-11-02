package flow.fileattach.domain.repository;

import flow.fileattach.domain.item.Ext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
class ExtRepositoryTest {

    @Autowired ExtRepository extRepository;

    @Test
    @DisplayName("확장자를 새로 등록한다.")
    @Transactional
    public void testSave() {
        //given
        Ext ext  = new Ext();
        String extName = "txt";
        ext.setExtName(extName);

        //when
        extRepository.save(ext);
        Ext findExt = extRepository.findById(extName);

        //then
        Assertions.assertThat(ext.getExtName()).isEqualTo(findExt.getExtName());
    }
}