package io.geekmind.budgie.controller;

import io.geekmind.budgie.model.dto.balance.BalanceType;
import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.repository.StandardAccountService;
import io.geekmind.budgie.repository.CategoryService;
import io.geekmind.budgie.repository.StandardBalanceService;
import io.geekmind.budgie.repository.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/records")
public class RecordControllerImpl {

    private final StandardBalanceService balanceService;
    private final StandardAccountService standardAccountService;
    private final CategoryService categoryService;
    private final RecordService recordService;

    @Autowired
    public RecordControllerImpl(StandardBalanceService balanceService,
                                StandardAccountService standardAccountService,
                                CategoryService categoryService,
                                RecordService recordService) {
        this.balanceService = balanceService;
        this.standardAccountService = standardAccountService;
        this.categoryService = categoryService;
        this.recordService = recordService;
    }

    @GetMapping
    public ModelAndView loadBalance(@RequestParam(name = "referenceDate", required = false)
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate referenceDate,
                                    @RequestParam(name = "accountId", required = false) Integer accountId,
                                    ModelAndView requestContext) {
        requestContext.addObject("balance", this.balanceService.generateBalance(accountId, referenceDate, BalanceType.REGULAR_PERIOD_BALANCE));
        requestContext.addObject("availableAccounts", this.standardAccountService.loadAll());
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
            ExistingRecord record = existingRecord.get();
            requestContext.addObject("balance", this.balanceService.generateBalance(
                record.getAccount().getId(), record.getRecordDate(), BalanceType.REGULAR_PERIOD_BALANCE
            ));
            requestContext.addObject("existingRecord", record);
            requestContext.addObject("availableAccounts", this.standardAccountService.loadAll());
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
            requestContext.setViewName(
                String.format(
                    "redirect:/records?referenceDate=%s&accountId=%s",
                    existingRecord.getRecordDate().toString(),
                    existingRecord.getAccount().getId()
                )
            );
        } else {
            requestContext.addObject("existingRecord", existingRecord);
            requestContext.addObject("availableAccounts", this.standardAccountService.loadAll());
            requestContext.addObject("availableCategories", this.categoryService.loadAll());
            requestContext.setViewName("records/edit.form");
        }
        return requestContext;
    }

    @GetMapping("/{id}")
    public ModelAndView showRecord(@PathVariable("id") Integer id,
                                   ModelAndView requestContext) {
        Optional<ExistingRecord> requestedRecord = this.recordService.loadById(id);
        if (requestedRecord.isPresent()) {
            requestContext.addObject("record", requestedRecord.get());
            requestContext.setViewName("records/show");
        }
        return requestContext;
    }

}
