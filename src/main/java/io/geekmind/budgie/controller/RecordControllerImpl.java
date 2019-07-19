package io.geekmind.budgie.controller;

import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.repository.AccountService;
import io.geekmind.budgie.repository.CategoryService;
import io.geekmind.budgie.repository.StandardBalanceService;
import io.geekmind.budgie.repository.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/records")
public class RecordControllerImpl {

    private final StandardBalanceService balanceService;
    private final AccountService accountService;
    private final CategoryService categoryService;
    private final RecordService recordService;

    @Autowired
    public RecordControllerImpl(StandardBalanceService balanceService,
                                AccountService accountService,
                                CategoryService categoryService,
                                RecordService recordService) {
        this.balanceService = balanceService;
        this.accountService = accountService;
        this.categoryService = categoryService;
        this.recordService = recordService;
    }

    @GetMapping
    public ModelAndView loadBalance(@RequestParam(name = "referenceDate", required = false)
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate referenceDate,
                                    @RequestParam(name = "accountId", required = false) Integer accountId,
                                    ModelAndView requestContext) {
        requestContext.addObject("balance", this.balanceService.generateBalance(accountId, referenceDate));
        requestContext.addObject("availableAccounts", this.accountService.loadAll());
        requestContext.setViewName("records/index");
        return requestContext;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView removeRecord(@PathVariable("id")Integer id,
                                     @RequestParam(name = "referenceDate", required = true) String referenceDate,
                                     @RequestParam(name = "accountId", required = true) String accountId,
                                     ModelAndView requestContext,
                                     RedirectAttributes redirectAttributes) {
        this.recordService.remove(id);
        redirectAttributes.addFlashAttribute("message", "The record was successfully removed.");
        requestContext.setViewName(
            String.format("redirect:/records?referenceDate=%s&accountId=%s", referenceDate, accountId)
        );
        return requestContext;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable("id") Integer id,
                                     ModelAndView requestContext,
                                     RedirectAttributes redirectAttributes) {
        Optional<ExistingRecord> existingRecord = this.recordService.loadById(id);
        if (existingRecord.isPresent()) {
            requestContext.addObject("existingRecord", existingRecord.get());
            requestContext.addObject("availableAccounts", this.accountService.loadAll());
            requestContext.addObject("availableCategories", this.categoryService.loadAll());
            requestContext.setViewName("records/edit.form");
        } else {
            redirectAttributes.addFlashAttribute("error", "Record not found.");
            requestContext.setViewName("redirect:/records");
        }
        return requestContext;
    }

    @PostMapping("/update")
    public ModelAndView updateRecord(@Valid ExistingRecord existingRecord,
                                     BindingResult bindingResult,
                                     ModelAndView requestContext,
                                     RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasErrors()) {
            this.recordService.update(existingRecord);
            redirectAttributes.addFlashAttribute("message", "Record successfully updated.");
            requestContext.setViewName("redirect:/records");
        } else {
            requestContext.addObject("existingRecord", existingRecord);
            requestContext.addObject("availableAccounts", this.accountService.loadAll());
            requestContext.addObject("availableCategories", this.categoryService.loadAll());
            requestContext.setViewName("records/edit.form");
        }
        return requestContext;
    }

}
