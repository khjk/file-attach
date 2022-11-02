package flow.fileattach.service;

import flow.fileattach.domain.item.Ext;
import flow.fileattach.domain.repository.ExtRepository;
import flow.fileattach.domain.repository.ExtSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExtService {

    private final ExtRepository extRepository;

    public Ext findExt(String extName) {
        Ext ext = extRepository.findById(extName);
        if (ext == null) {
            throw new IllegalArgumentException("해당 확장자가 없습니다. ext=" + extName);
        }
        return ext;
    }

    public List<Ext> findForbiddenExt() {
        ExtSearch extSearch = new ExtSearch();
        extSearch.setIsForbidden(true);
        return findExtByCondition(extSearch);
    }

    public List<Ext> findFixedExt() {
        ExtSearch extSearch = new ExtSearch();
        extSearch.setIsFixed(true);
        return findExtByCondition(extSearch);
    }

    public List<Ext> findExtByCondition(ExtSearch extSearch) {
        return extRepository.findExtByCondition(extSearch);
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
