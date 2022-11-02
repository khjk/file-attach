package flow.fileattach.util;

import flow.fileattach.domain.item.AttachFile;
import flow.fileattach.domain.item.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 파일 저장과 관련된 업무 처리
 */
@Component
public class FileUtil {

    @Value("${file.dir}")
    private String fileDir;

    /**
     * 파일 저장 경로 반환
     */
    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    /**
     * 파일 목록 저장
     */
    public List<AttachFile> storeFiles(List<MultipartFile> multipartFiles, Document document) throws IOException {
        List<AttachFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multiPartFile : multipartFiles) {
            if (!multipartFiles.isEmpty()) {
                storeFileResult.add(storeFile(multiPartFile, document));
            }
        }
        return storeFileResult;
    }

    /**
     * 파일저장
     */
    public AttachFile storeFile(MultipartFile multipartFile, Document document) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originalFileName = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFileName);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        String ext = extractExt(originalFileName);

        return new AttachFile(document, originalFileName, storeFileName, ext);
    }

    /**
     * UUID를 합해서 파일명이 같더라도 서버에는 다르게 저장
     */
    private String createStoreFileName(String originalFileName) {
        String ext = extractExt(originalFileName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    /**
     * 확장자 추출
     */
    private String extractExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos + 1);
    }
}
