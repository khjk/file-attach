package flow.fileattach.service;

import flow.fileattach.domain.item.Ext;
import flow.fileattach.domain.repository.ExtRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExtService {

    private final ExtRepository extRepository;

    private Ext findExt(String extName) {
        Ext ext = extRepository.findById(extName);
        if (ext == null) {
            throw new IllegalArgumentException("해당 확장자가 없습니다. ext=" + extName);
        } else if (ext.getFixedYn() == true) {
            throw new IllegalArgumentException("고정 확장자는 삭제할 수 없습니다. ext=" + extName);
        }
        return ext;
    }

    @Transactional
    public void saveExt(String extName) {
        Ext ext = new Ext();
        ext.setExtName(extName);
        ext.setFixedYn(false);
        ext.setCheckedYn(true);

        extRepository.save(ext);
    }

    @Transactional
    public void updateExt(String extName, Boolean isChecked) {
        Ext ext = findExt(extName);
        ext.setCheckedYn(isChecked);
    }

    @Transactional
    public void delete(String extName) {
        extRepository.delete(extName);
    }


}
