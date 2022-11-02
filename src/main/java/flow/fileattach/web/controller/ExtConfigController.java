package flow.fileattach.web.controller;

import flow.fileattach.service.ExtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ExtConfigController {
    private final ExtService extService;

    /**
     * 확장자 수정 폼을 보여준다.
     */
    @GetMapping("/ext/edit")
    public String newFile() {
        return "/ext/ext-edit";
    }

    /**
     * 체크박스가 선택될때마다 폼의 데이터를 저장한다.
     */
    @PostMapping("/ext/edit")
    public String updateExt(@PathVariable String extName, @PathVariable Boolean isChecked) {
        extService.updateExt(extName, isChecked);

        return "redirect:/ext/ext-edit";
    }

    /**
     * 새로운 확장자를 저장한다.
     */
    public String addExt(@PathVariable String extName) {
        extService.saveExt(extName);

        return "redirect:/ext/ext-edit";
    }

    /**
     * X를 누르면 삭제된다.
     */
    public String removeExt(@PathVariable String extName) {
        extService.delete(extName);

        return "redirect:/ext/ext-edit";
    }



}
