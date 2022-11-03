package flow.fileattach.web.controller;


import flow.fileattach.domain.item.AttachFile;
import flow.fileattach.service.ExtService;
import flow.fileattach.service.FileService;
import flow.fileattach.util.FileUtil;
import flow.fileattach.web.dto.FileForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UploadController {

    private final FileService fileService;
    private final ExtService extService;
    private final FileUtil fileUtil;
    /**
     * 등록 폼을 보여준다.
     */
    @GetMapping("files/new")
    public String newFile(Model model) {
        //금지된 확장자 목록 가져오기
        String forbiddenExt = extService.findForbiddenExt().stream()
                .map(i -> i.getExtName()).collect(Collectors.joining(", "));
        model.addAttribute("forbiddenExt", forbiddenExt);

        return "upload/upload-form";
    }

    /**
     * 폼의 데이터를 저장하고 보여주는 화면으로 리다이렉트한다.
     */
    @PostMapping("files/new")
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

    /**
     * 파일 다운로드
     */
    @GetMapping("attach/{fileId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long fileId) throws MalformedURLException {
        AttachFile file = fileService.findFile(fileId);

        //서버에 저장된 파일명
        String storeFileName = file.getStoreFileName();
        //고객이 업로드한 파일명
        String uploadFileName = file.getUploadFileName();

        UrlResource resource = new UrlResource("file:" + fileUtil.getFullPath(storeFileName));

        log.info("업로드파일명={}", uploadFileName);
        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

}
