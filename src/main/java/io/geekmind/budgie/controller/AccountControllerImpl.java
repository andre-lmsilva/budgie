package io.geekmind.budgie.controller;

import io.geekmind.budgie.model.dto.NewAccount;
import io.geekmind.budgie.repository.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/accounts")
public class AccountControllerImpl {

    private final AccountService accountService;

    @Autowired
    public AccountControllerImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ModelAndView loadAccounts(ModelAndView requestContext) {
        requestContext.addObject("availableAccounts", this.accountService.loadAll());
        requestContext.setViewName("accounts/index");
        return requestContext;
    }

    @GetMapping("/new")
    public ModelAndView showNewAccountForm(ModelAndView requestContext) {
        requestContext.addObject("newAccount", new NewAccount());
        requestContext.setViewName("accounts/new.form");
        return requestContext;
    }

    @PostMapping("/create")
    public ModelAndView createNewAccount(@Valid NewAccount newAccount,
                                         BindingResult bindingResult,
                                         ModelAndView requestContext) {
        if (bindingResult.hasErrors()) {
            requestContext.addObject("newAccount", newAccount);
            requestContext.setViewName("accounts/new.form");
        } else {
            this.accountService.create(newAccount);
            requestContext.setViewName("redirect:/accounts");
        }
        return requestContext;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView removeAccount(@PathVariable("id")Integer id, ModelAndView requestContext) {
        this.accountService.remove(id);
        requestContext.setViewName("redirect:/accounts");
        return requestContext;
    }

}
