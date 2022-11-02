package flow.fileattach.web.controller;


import flow.fileattach.service.FileService;
import flow.fileattach.util.FileUtil;
import flow.fileattach.web.dto.FileForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UploadController {

    private final FileService fileService;

    /**
     * 등록 폼을 보여준다.
     */
    @GetMapping("/files/new")
    public String newFile() {
        return "/upload/upload-form";
    }

    /**
     * 폼의 데이터를 저장하고 보여주는 화면으로 리다이렉트한다.
     */
    @PostMapping("/files/new")
    public String saveFile(@ModelAttribute FileForm form, RedirectAttributes redirectAttributes) throws IOException {
        Long fileId = fileService.saveFile(form);

        redirectAttributes.addAttribute("fileId", fileId);

        return "redirect:/files/{fileId}";
    }


}
