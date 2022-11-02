package flow.fileattach.service;

import flow.fileattach.domain.item.AttachFile;
import flow.fileattach.domain.item.Ext;
import flow.fileattach.domain.repository.ExtRepository;
import flow.fileattach.domain.repository.ExtSearch;
import flow.fileattach.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * 금지된 업로드 항목이 있는지 검사
     */
    public String hasForbiddenExt(List<MultipartFile> multipartFiles) throws IOException {
        List<Ext> forbiddenLst = findForbiddenExt();
        List<AttachFile> storeFileResult = new ArrayList<>();
        Set<String> set = new HashSet<>();
        for (MultipartFile multiPartFile : multipartFiles) {
            String ext = FileUtil.extractExt(multiPartFile.getOriginalFilename());
            Optional<Ext> check = forbiddenLst.stream().filter(i -> ext.equalsIgnoreCase(i.getExtName())).findAny();
            if (check.isPresent()) {
                set.add(check.get().getExtName());
            }
        }

        return set.stream().collect(Collectors.joining(", "));
    }


}
