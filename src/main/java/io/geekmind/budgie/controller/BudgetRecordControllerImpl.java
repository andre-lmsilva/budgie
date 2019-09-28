package io.geekmind.budgie.controller;

import io.geekmind.budgie.model.dto.ExistingRecord;
import io.geekmind.budgie.repository.BudgetRecordService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/budgetRecords")
public class BudgetRecordControllerImpl {

    private final BudgetRecordService budgetRecordService;

    public BudgetRecordControllerImpl(BudgetRecordService budgetRecordService) {
        this.budgetRecordService = budgetRecordService;
    }

    @GetMapping("/apply/{id}")
    public ModelAndView applyBudgetTemplateRecord(@PathVariable("id")Integer budgetTemplateRecordId,
                                                  @RequestParam(name = "periodStartDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodStartDate,
                                                  @RequestParam(name = "periodEndDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodEndDate,
                                                  ModelAndView requestContext) {

        requestContext.setViewName("redirect:/records");
        this.budgetRecordService.create(budgetTemplateRecordId, periodStartDate, periodEndDate)
            .ifPresent(record ->
                requestContext.setViewName(
                    String.format(
                        "redirect:/records?accountId=%s&referenceDate=%s",
                        record.getAccount().getId(),
                        periodStartDate
                    )
                )
            );
        return requestContext;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable("id")Integer budgetRecordId,
                               @RequestParam(name="accountId") Integer accountId,
                               @RequestParam(name="referenceDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate referenceDate,
                               ModelAndView requestContext,
                               RedirectAttributes redirectAttributes) {

        this.budgetRecordService.remove(budgetRecordId);
        requestContext.setViewName(
            String.format("redirect:/records?accountId=%s&referenceDate=%s", accountId, referenceDate)
        );
        redirectAttributes.addFlashAttribute("message", "The record was successfully removed.");
        return requestContext;
    }

}
