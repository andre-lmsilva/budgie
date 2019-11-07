package io.geekmind.budgie.controller;

import io.geekmind.budgie.model.dto.account.EditAccount;
import io.geekmind.budgie.model.dto.account.NewAccount;
import io.geekmind.budgie.repository.AccountService;
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
@RequestMapping("/accounts")
public class AccountControllerImpl {

    private final AccountService accountService;
    private final CurrencyService currencyService;

    @Autowired
    public AccountControllerImpl(AccountService accountService,
                                 CurrencyService currencyService) {
        this.accountService = accountService;
        this.currencyService = currencyService;
    }

    @GetMapping
    public ModelAndView loadAccounts(ModelAndView requestContext) {
        requestContext.addObject("availableAccounts", this.accountService.loadAll());
        requestContext.setViewName("accounts/index");
        return requestContext;
    }

    @GetMapping("/new")
    public ModelAndView showNewAccountForm(ModelAndView requestContext) {
        requestContext.addObject("availableAccounts", this.accountService.loadAll());
        requestContext.addObject("availableCurrencies", this.currencyService.loadAll());
        requestContext.addObject("newAccount", new NewAccount());
        requestContext.setViewName("accounts/new.form");
        return requestContext;
    }

    @PostMapping("/create")
    public ModelAndView createNewAccount(@Valid NewAccount newAccount,
                                         BindingResult bindingResult,
                                         ModelAndView requestContext,
                                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            requestContext.addObject("availableAccounts", this.accountService.loadAll());
            requestContext.addObject("availableCurrencies", this.currencyService.loadAll());
            requestContext.addObject("newAccount", newAccount);
            requestContext.setViewName("accounts/new.form");
        } else {
            this.accountService.create(newAccount);
            redirectAttributes.addFlashAttribute("message", "Account record successfully created.");
            requestContext.setViewName("redirect:/accounts/new");
        }
        return requestContext;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView removeAccount(@PathVariable("id")Integer id,
                                      ModelAndView requestContext,
                                      RedirectAttributes redirectAttributes) {
        this.accountService.remove(id);
        redirectAttributes.addFlashAttribute("message", "Account record successfully removed.");
        requestContext.setViewName("redirect:/accounts");
        return requestContext;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable("id") Integer id,
                                     ModelAndView requestContext,
                                     RedirectAttributes redirectAttributes) {
        Optional<EditAccount> editAccount = this.accountService.loadByIdForEdit(id);
        if (editAccount.isPresent()) {
            requestContext.addObject("availableAccounts", this.accountService.loadAllExcept(id));
            requestContext.addObject("availableCurrencies", this.currencyService.loadAll());
            requestContext.addObject("editAccount", editAccount.get());
            requestContext.setViewName("accounts/edit.form");
        } else {
            redirectAttributes.addFlashAttribute("error", "Account not found.");
            requestContext.setViewName("redirect:/accounts");
        }
        return requestContext;
    }

    @PostMapping("/update")
    public ModelAndView updateAccount(@Valid EditAccount editAccount,
                               BindingResult bindingResult,
                               ModelAndView requestContext,
                               RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasErrors()) {
            this.accountService.update(editAccount);
            redirectAttributes.addFlashAttribute("message", "Account successfully updated.");
            requestContext.setViewName("redirect:/accounts");
        } else {
            requestContext.addObject("availableAccounts", this.accountService.loadAllExcept(editAccount.getId()));
            requestContext.addObject("availableCurrencies", this.currencyService.loadAll());
            requestContext.addObject("editAccount", editAccount);
            requestContext.setViewName("accounts/edit.form");
        }
        return requestContext;
    }

}
