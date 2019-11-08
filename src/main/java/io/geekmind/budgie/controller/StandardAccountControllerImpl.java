package io.geekmind.budgie.controller;

import io.geekmind.budgie.model.dto.standard_account.EditStandardAccount;
import io.geekmind.budgie.model.dto.standard_account.NewStandardAccount;
import io.geekmind.budgie.repository.StandardAccountService;
import io.geekmind.budgie.repository.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/standard_accounts")
public class StandardAccountControllerImpl {

    private final StandardAccountService standardAccountService;
    private final CurrencyService currencyService;

    @Autowired
    public StandardAccountControllerImpl(StandardAccountService standardAccountService,
                                         CurrencyService currencyService) {
        this.standardAccountService = standardAccountService;
        this.currencyService = currencyService;
    }

    @GetMapping
    public ModelAndView loadAccounts(ModelAndView requestContext) {
        requestContext.addObject("availableAccounts", this.standardAccountService.loadAll());
        requestContext.setViewName("standard_accounts/index");
        return requestContext;
    }

    @GetMapping("/new")
    public ModelAndView showNewAccountForm(ModelAndView requestContext) {
        requestContext.addObject("availableAccounts", this.standardAccountService.loadAll());
        requestContext.addObject("availableCurrencies", this.currencyService.loadAll());
        requestContext.addObject("newAccount", new NewStandardAccount());
        requestContext.setViewName("standard_accounts/new.form");
        return requestContext;
    }

    @PostMapping("/create")
    public ModelAndView createNewAccount(@Valid NewStandardAccount newAccount,
                                         BindingResult bindingResult,
                                         ModelAndView requestContext,
                                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            requestContext.addObject("availableAccounts", this.standardAccountService.loadAll());
            requestContext.addObject("availableCurrencies", this.currencyService.loadAll());
            requestContext.addObject("newAccount", newAccount);
            requestContext.setViewName("standard_accounts/new.form");
        } else {
            this.standardAccountService.create(newAccount);
            redirectAttributes.addFlashAttribute("message", "Account record successfully created.");
            requestContext.setViewName("redirect:/standard_accounts/new");
        }
        return requestContext;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView removeAccount(@PathVariable("id")Integer id,
                                      ModelAndView requestContext,
                                      RedirectAttributes redirectAttributes) {
        this.standardAccountService.remove(id);
        redirectAttributes.addFlashAttribute("message", "Account record successfully archived.");
        requestContext.setViewName("redirect:/standard_accounts");
        return requestContext;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable("id") Integer id,
                                     ModelAndView requestContext,
                                     RedirectAttributes redirectAttributes) {
        Optional<EditStandardAccount> editAccount = this.standardAccountService.loadByIdForEdit(id);
        if (editAccount.isPresent()) {
            requestContext.addObject("availableAccounts", this.standardAccountService.loadAllExcept(id));
            requestContext.addObject("availableCurrencies", this.currencyService.loadAll());
            requestContext.addObject("editAccount", editAccount.get());
            requestContext.setViewName("standard_accounts/edit.form");
        } else {
            redirectAttributes.addFlashAttribute("error", "Account not found.");
            requestContext.setViewName("redirect:/standard_accounts");
        }
        return requestContext;
    }

    @PostMapping("/update")
    public ModelAndView updateAccount(@Valid EditStandardAccount editAccount,
                               BindingResult bindingResult,
                               ModelAndView requestContext,
                               RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasErrors()) {
            this.standardAccountService.update(editAccount);
            redirectAttributes.addFlashAttribute("message", "Account successfully updated.");
            requestContext.setViewName("redirect:/standard_accounts");
        } else {
            requestContext.addObject("availableAccounts", this.standardAccountService.loadAllExcept(editAccount.getId()));
            requestContext.addObject("availableCurrencies", this.currencyService.loadAll());
            requestContext.addObject("editAccount", editAccount);
            requestContext.setViewName("standard_accounts/edit.form");
        }
        return requestContext;
    }

}
