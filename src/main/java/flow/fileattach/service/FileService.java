package flow.fileattach.service;

import flow.fileattach.domain.repository.FileRepository;
import flow.fileattach.domain.item.Document;
import flow.fileattach.domain.item.AttachFile;
import flow.fileattach.util.FileUtil;
import flow.fileattach.web.dto.FileForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FileUtil fileUtil;

    public Document findDocument(Long documentId) {
        return fileRepository.findDocument(documentId);
    }

    public List<Document> findAllDocuments() {return fileRepository.findAllDocuments();}

    public AttachFile findFile(Long fileId) {
        return fileRepository.findFile(fileId);
    }


    @Transactional
    public Long saveFile(FileForm form) throws IOException {
        //문건정보 생성
        Document doc = new Document();
        doc.setDescription(form.getDescription());
        List<AttachFile> fileVOLst = fileUtil.storeFiles(form.getAttachFiles(), doc);
        doc.setAttachFiles(fileVOLst);

        for(AttachFile file : fileVOLst) {
            log.info("서버저장 파일명={}" + file.getStoreFileName());
        }

        fileRepository.save(doc);

        return doc.getId();
    }


}
