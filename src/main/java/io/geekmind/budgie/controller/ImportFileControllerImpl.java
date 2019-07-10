package io.geekmind.budgie.controller;

import io.geekmind.budgie.model.dto.NewImportedFile;
import io.geekmind.budgie.repository.CategoryService;
import io.geekmind.budgie.repository.ImportedFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/importFiles")
public class ImportFileControllerImpl {

    private final ImportedFileService importedFileService;
    private final CategoryService categoryService;

    @Autowired
    public ImportFileControllerImpl(ImportedFileService importedFileService,
                                    CategoryService categoryService) {
        this.importedFileService = importedFileService;
        this.categoryService = categoryService;
    }

    @GetMapping("/new")
    public ModelAndView showUploadForm(ModelAndView requestContext) {
        requestContext.setViewName("importFiles/new.form");
        return requestContext;
    }

    @PostMapping("/upload")
    public ModelAndView uploadFile(MultipartFile csvFile,
                                   ModelAndView requestContext) {
        requestContext.addObject("importedFile", this.importedFileService.create(csvFile));
        requestContext.addObject("availableCategories", this.categoryService.loadAll());
        requestContext.setViewName("importFiles/list.records");
        return requestContext;
    }

    @PostMapping("/importRecords")
    public ModelAndView importRecords(@Valid NewImportedFile newImportedFile,
                                      BindingResult bindingResult,
                                      ModelAndView requestContext,
                                      RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            requestContext.addObject("importedFile", newImportedFile);
            requestContext.addObject("availableCategories", this.categoryService.loadAll());
            requestContext.setViewName("importFiles/list.records");
        } else {
            this.importedFileService.saveRecords(newImportedFile);
            redirectAttributes.addFlashAttribute("message", "File successfully imported.");
            requestContext.setViewName("redirect:/records");
        }
        return requestContext;
    }

    @GetMapping
    public ModelAndView loadAll(ModelAndView requestContext) {
        requestContext.addObject("availableImportedFiles", this.importedFileService.loadAll());
        requestContext.setViewName("importFiles/index");
        return requestContext;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView removeImportedFile(@PathVariable("id")Integer id,
                                           ModelAndView requestContext,
                                           RedirectAttributes redirectAttributes) {
        this.importedFileService.remove(id);
        redirectAttributes.addFlashAttribute("message", "Imported file successfully removed.");
        requestContext.setViewName("redirect:/importFiles");
        return requestContext;
    }

}
