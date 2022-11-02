package flow.fileattach.web.controller;

import flow.fileattach.domain.item.Document;
import flow.fileattach.service.FileService;
import flow.fileattach.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ViewerController {

    private final FileService fileService;
    private final FileUtil fileUtil;

    @RequestMapping("/")
    public String home() {
        return "home";
    }

    /**
     * 파일을 보여준다
     */
    @GetMapping("/files/{id}")
    public String showFiles(@PathVariable Long id, Model model) {
        Document fileVO = fileService.findDocument(id);

        model.addAttribute("file", fileVO);

        return "/upload/file-view";
    }

    /**
     * 문건목록을 보여준다
     */
    @GetMapping("/files")
    public String showDocLst(Model model) {
        List<Document> allDoc = fileService.findAllDocuments();

        model.addAttribute("documents", allDoc);

        return "/upload/doc-view";
    }

}
