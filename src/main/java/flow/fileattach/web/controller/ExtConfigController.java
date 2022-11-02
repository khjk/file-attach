package flow.fileattach.web.controller;

import flow.fileattach.domain.item.Ext;
import flow.fileattach.domain.repository.ExtSearch;
import flow.fileattach.service.ExtService;
import flow.fileattach.web.dto.ExtForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ExtConfigController {
    private final ExtService extService;
    private final String MAX_EXT_SIZE = "200";

    @Cacheable("/fixedExt")
    public List<Ext> getFixedExtList() {
        return extService.findFixedExt();
    }

    /**
     * 사용자가 추가한 확장자 목록 반환한다.
     */
    public List<Ext> getAddedExtList() {
        ExtSearch extSearch = new ExtSearch();
        extSearch.setIsForbidden(true);
        extSearch.setIsFixed(false); //새로 추가된 것들
        return extService.findExtByCondition(extSearch);
    }

    /**
     * 확장자 수정 폼을 보여준다.
     */
    @GetMapping("/ext/edit")
    public String newFile(Model model) {
        model.addAttribute("fixedExt", getFixedExtList());
        List<Ext> addedLst = getAddedExtList();
        model.addAttribute("addedExt", addedLst);
        model.addAttribute("availSize", addedLst.size() + "/" + MAX_EXT_SIZE);
        return "/ext/ext-edit";
    }

    /**
     * 체크박스가 선택될때마다 폼의 데이터를 저장한다.
     */
    @PutMapping("/ext/edit/{extName}")
    public ResponseEntity<List<Ext>> updateExt(@PathVariable String extName, @RequestBody ExtForm extForm) {
        extService.updateExt(extName, extForm.getIsChecked());

        return new ResponseEntity<>(extService.findFixedExt(), HttpStatus.OK);
    }

    /**
     * 새로운 확장자를 저장한다.
     */
    @PostMapping("/ext/edit")
    public ResponseEntity<ExtForm> addExt(@RequestBody ExtForm extForm) {
        String extName = extForm.getExtName();
        if (extName.isEmpty()) {
            throw new IllegalArgumentException("확장자명은 필수 입력값입니다");
        }
        if (!isEng(extName)) {
            throw new IllegalArgumentException("확장자명은 영문만 입력 가능합니다.");
        }
        if (extName.length() > 20) {
            throw new IllegalArgumentException("확장자 길이는 최대 20자까지 허용됩니다.");
        }

        //고정확장자여부 - 캐시된 고정확장자에서 가져와서 체크
        Optional<Ext> result = extService.findFixedExt().stream().filter(i -> extName.equals(i.getExtName())).findAny();
        if (result.isPresent()) {
            throw new IllegalArgumentException("고정확장자는 커스텀확장자로 등록할 수 없습니다.");
        }

        //기등록여부
        boolean isExisted = true;
        try {
            extService.findExt(extName);
        } catch (IllegalArgumentException e) {
            isExisted = false;
        }
        if (isExisted) {
            throw new IllegalArgumentException("기등록된 확장자입니다.");
        }

        //200개 제한
        int now = (int) extService.getNotFixedExtCnt();
        if (now == Integer.parseInt(MAX_EXT_SIZE)) {
            throw new IllegalArgumentException("최대 " + MAX_EXT_SIZE + "개까지 저장 가능합니다.");
        }
        extService.saveExt(extForm.getExtName());

        return new ResponseEntity<>(extForm, HttpStatus.OK);
    }

    /**
     * X를 누르면 삭제된다.
     */
    @DeleteMapping("/ext/edit/{extValue}")
    public ResponseEntity<ExtForm> removeExt(@RequestBody ExtForm extForm) {
        String extName = extForm.getExtName();
        //안겹치는지 확인
        if (extService.findExt(extName) == null) {
            throw new IllegalArgumentException("등록된 확장자가 없습니다.");
        }
        extService.delete(extName);

        return new ResponseEntity<>(extForm, HttpStatus.OK);
    }

    /**
     * 영문 여부 확인
     */
    public boolean isEng(String word) {
        return !Pattern.matches("^[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*$", word);
    }


}
