package flow.fileattach.web.controller;

import flow.fileattach.domain.item.AttachFile;
import flow.fileattach.service.FileService;
import flow.fileattach.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DownloadController {

    private final FileService fileService;
    private final FileUtil fileUtil;

    /**
     * 파일 다운로드
     * 파일 다운로드 시 권한 체크같은 복잡한 상황까지 가정한다 생각하고 이미지 id 를 요청하도록 했다. 파일
     * 다운로드시에는 고객이 업로드한 파일 이름으로 다운로드 하는게 좋다. 이때는 Content-Disposition
     * 해더에 attachment; filename="업로드 파일명" 값을 주면 된다.
     */
    @GetMapping("/attach/{fileId}")
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
