package io.geekmind.budgie.controller;

import io.geekmind.budgie.model.dto.ExistingAccount;
import io.geekmind.budgie.model.dto.NewBudgetTemplateRecord;
import io.geekmind.budgie.repository.AccountService;
import io.geekmind.budgie.repository.BudgetTemplateRecordService;
import io.geekmind.budgie.repository.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Provides all the required capabilities to manage planned incomes and expenses and replicate them across periods.
 *
 * @author Andre Silva
 */
@Service
@RequestMapping("/budget")
public class BudgetController {

    private final AccountService accountService;
    private final CategoryService categoryService;
    private final BudgetTemplateRecordService budgetTemplateRecordService;

    public BudgetController(AccountService accountService,
                            CategoryService categoryService,
                            BudgetTemplateRecordService budgetTemplateRecordService) {
        this.accountService = accountService;
        this.categoryService = categoryService;
        this.budgetTemplateRecordService = budgetTemplateRecordService;
    }

    @GetMapping
    public ModelAndView loadRecordsFor(@RequestParam(name = "accountId", required = false) Integer accountId,
                                       ModelAndView requestContext) {

        ExistingAccount selectedAccount = null;
        if (null == accountId) {
            selectedAccount = this.accountService.getMainAccount().orElse(null);
        } else {
            selectedAccount = this.accountService.loadById(accountId).orElse(null);
        }

        requestContext.addObject("selectedAccount", selectedAccount);
        requestContext.addObject("availableAccounts", this.accountService.loadAll());
        requestContext.addObject("availableRecords",
            this.budgetTemplateRecordService.loadAllFromAccount(selectedAccount.getId())
        );
        requestContext.setViewName("budget/index");
        return requestContext;
    }

    @GetMapping("/new")
    public ModelAndView showNewForm(ModelAndView requestContext) {
        requestContext.addObject("availableAccounts", this.accountService.loadAll());
        requestContext.addObject("availableCategories", this.categoryService.loadAll());
        requestContext.addObject("newBudgetTemplateRecord", new NewBudgetTemplateRecord());
        requestContext.setViewName("budget/new.form");
        return requestContext;
    }

    @PostMapping("/create")
    public ModelAndView createNewBudgetTemplateRecord(@Validated NewBudgetTemplateRecord newBudgetTemplateRecord,
                                                      BindingResult bindingResult,
                                                      RedirectAttributes redirectAttributes,
                                                      ModelAndView requestContext) {
        if (!bindingResult.hasErrors()) {
            this.budgetTemplateRecordService.create(newBudgetTemplateRecord);
            redirectAttributes.addFlashAttribute("message", "Budget template record successfully created!");
            requestContext.setViewName("redirect:/budget/new");
        } else {
            requestContext.addObject("newBudgetTemplateRecord", newBudgetTemplateRecord);
            requestContext.addObject("availableAccounts", this.accountService.loadAll());
            requestContext.addObject("availableCategories", this.categoryService.loadAll());
            requestContext.setViewName("budget/new.form");
        }
        return requestContext;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView removeBudgetTemplateRecord(@PathVariable("id")Integer id,
                                                   ModelAndView requestContext,
                                                   RedirectAttributes redirectAttributes) {
        requestContext.setViewName("redirect:/budget");
        this.budgetTemplateRecordService.remove(id)
            .ifPresent(record -> {
                redirectAttributes.addFlashAttribute("message", "Budget template record successfully removed!");
                requestContext.setViewName(String.format("redirect:/budget?accountId=%s", record.getAccount().getId()));
            });
        return requestContext;
    }

}
