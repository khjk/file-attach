package flow.fileattach.web.controller;


import flow.fileattach.service.ExtService;
import flow.fileattach.service.FileService;
import flow.fileattach.util.exception.ErrorResult;
import flow.fileattach.web.dto.FileForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UploadController {
    private final FileService fileService;
    private final ExtService extService;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResult> illegalExHandle(IllegalArgumentException e) {
        log.error("[exceptionHandle] ex", e);
        return new ResponseEntity(new ErrorResult("BAD", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * 등록 폼을 보여준다.
     */
    @GetMapping("/files/new")
    public String newFile(Model model) {
        //금지된 확장자 목록 가져오기
        String forbiddenExt = extService.findForbiddenExt().stream()
                .map(i -> i.getExtName()).collect(Collectors.joining(", "));
        model.addAttribute("forbiddenExt", forbiddenExt);

        return "/upload/upload-form";
    }

    /**
     * 폼의 데이터를 저장하고 보여주는 화면으로 리다이렉트한다.
     */
    @PostMapping("/files/new")
    public ResponseEntity<FileForm> saveFile(@ModelAttribute FileForm form) throws IOException {
        //업로드 금지 확장자 여부 체크
        String forbiddenExt = extService.hasForbiddenExt(form.getAttachFiles());
        if(!forbiddenExt.isEmpty()) {
            throw new IllegalArgumentException("첨부불가 확장자 [ " + forbiddenExt + " ]가 포함되어 있습니다.");
        }
        //파일저장
        Long fileId = fileService.saveFile(form);

        FileForm fileForm = new FileForm();
        fileForm.setFileId(fileId);

        return new ResponseEntity<>(fileForm, HttpStatus.OK);
    }


}
