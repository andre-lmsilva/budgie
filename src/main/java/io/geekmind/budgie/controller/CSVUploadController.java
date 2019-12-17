package io.geekmind.budgie.controller;

import io.geekmind.budgie.csv.CSVParserService;
import io.geekmind.budgie.model.dto.ImportCSVRecords;
import io.geekmind.budgie.model.dto.NewSingleRecord;
import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import io.geekmind.budgie.repository.CategoryService;
import io.geekmind.budgie.repository.SingleRecordService;
import io.geekmind.budgie.repository.StandardAccountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Controller
@RequestMapping("/csv_upload")
public class CSVUploadController {

    private final StandardAccountService standardAccountService;
    private final CSVParserService csvParserService;
    private final CategoryService categoryService;
    private final SingleRecordService singleRecordService;

    public CSVUploadController(StandardAccountService standardAccountService,
                               CSVParserService csvParserService,
                               CategoryService categoryService,
                               SingleRecordService singleRecordService) {
        this.standardAccountService = standardAccountService;
        this.csvParserService = csvParserService;
        this.categoryService = categoryService;
        this.singleRecordService = singleRecordService;
    }

    @GetMapping("/new")
    public ModelAndView showUploadForm(ModelAndView requestContext) {
        requestContext.addObject("availableAccounts", this.standardAccountService.loadAll());
        requestContext.setViewName("csv_upload/new.form");
        return requestContext;
    }

    @PostMapping("/upload")
    public ModelAndView uploadFile(@RequestParam("csvFile") MultipartFile csfFile,
                                   @RequestParam("accountId") Integer accountId,
                                   ModelAndView requestContext) throws IOException {
        Optional<ExistingStandardAccount> selectedAccount = this.standardAccountService.loadById(accountId);

        if (selectedAccount.isPresent()) {
            ExistingStandardAccount existingStandardAccount = selectedAccount.get();
            ImportCSVRecords parsedRecords = new ImportCSVRecords();
            parsedRecords.setCsvRecords(
                this.csvParserService.parseCSVFile(csfFile, existingStandardAccount)
            );
            requestContext.addObject("parsedRecords", parsedRecords);
            requestContext.addObject("availableCategories", this.categoryService.loadAll());
            requestContext.addObject("selectedAccount", existingStandardAccount);
            requestContext.setViewName("csv_upload/edit.form");
        } else {
            requestContext.addObject("availableAccounts", this.standardAccountService.loadAll());
            requestContext.addObject("error", "Account not found.");
            requestContext.setViewName("csv_upload/new.form");
        }
        return requestContext;
    }

    @PostMapping("/import")
    public ModelAndView importRecords(@Valid ImportCSVRecords parsedRecords,
                                      ModelAndView requestContext,
                                      RedirectAttributes redirectAttributes) {
        parsedRecords.getCsvRecords().forEach(this.singleRecordService::create);
        redirectAttributes.addFlashAttribute("message", "Records successfully imported.");
        requestContext.setViewName("redirect:/records");
        return requestContext;
    }


}
