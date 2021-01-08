package io.geekmind.budgie.controller;

import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.model.entity.Attachment;
import io.geekmind.budgie.repository.AttachmentService;
import io.geekmind.budgie.repository.RecordService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/attachments")
public class AttachmentControllerImpl {

    private final RecordService recordService;
    private final AttachmentService attachmentService;

    public AttachmentControllerImpl(RecordService recordService,
                                    AttachmentService attachmentService) {
        this.recordService = recordService;
        this.attachmentService = attachmentService;
    }

    @GetMapping
    public ModelAndView listAttachmentsForRecord(@RequestParam(name = "recordId")Integer recordId,
                                                 ModelAndView request) {
        this.recordService.loadById(recordId)
            .map((ExistingRecord existingRecord) -> request.addObject("existingRecord", existingRecord));
        request.setViewName("attachments/index");
        return request;
    }

    @PostMapping("/upload")
    public ModelAndView uploadFile(@RequestParam("attachmentFile") MultipartFile attachmentFile,
                                   @RequestParam("recordId") Integer recordId,
                                   ModelAndView requestContext,
                                   RedirectAttributes redirectAttributes) {
        this.attachmentService.create(recordId, attachmentFile);
        redirectAttributes.addFlashAttribute("message", "File attached successfully.");
        requestContext.setViewName(String.format("redirect:/attachments?recordId=%s", recordId));
        return requestContext;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView removeFile(@PathVariable("id") Integer attachmentId,
                                   ModelAndView modelAndView,
                                   RedirectAttributes redirectAttributes) {
        Integer recordId = this.attachmentService.delete(attachmentId);
        redirectAttributes.addFlashAttribute("message", "Attachment successfully removed.");
        modelAndView.setViewName(
            String.format("redirect:/attachments?recordId=%s", recordId)
        );
        return modelAndView;
    }

    @GetMapping("/download/{id}")
    public void downloadFile(@PathVariable("id") Integer attachmentId,
                             HttpServletResponse response) throws IOException {
        this.attachmentService.downloadFile(attachmentId, response);
        response.getOutputStream().flush();
    }

}
