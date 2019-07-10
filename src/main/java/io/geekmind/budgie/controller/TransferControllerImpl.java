package io.geekmind.budgie.controller;

import io.geekmind.budgie.model.dto.NewTransfer;
import io.geekmind.budgie.repository.AccountService;
import io.geekmind.budgie.repository.CategoryService;
import io.geekmind.budgie.repository.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/transfers")
public class TransferControllerImpl {

    private final CategoryService categoryService;
    private final AccountService accountService;
    private final TransferService transferService;

    @Autowired
    public TransferControllerImpl(CategoryService categoryService,
                                  AccountService accountService,
                                  TransferService transferService) {
        this.categoryService = categoryService;
        this.accountService = accountService;
        this.transferService = transferService;
    }

    @GetMapping("/new")
    public ModelAndView showNewForm(ModelAndView requestContext) {
        requestContext.addObject("availableCategories", this.categoryService.loadAll());
        requestContext.addObject("availableAccounts", this.accountService.loadNonDependantAccounts());
        requestContext.addObject("newTransfer", new NewTransfer());
        requestContext.setViewName("transfers/new.form");
        return requestContext;
    }

    @PostMapping("/create")
    public ModelAndView createNewTransfer(@Valid NewTransfer newTransfer,
                                          BindingResult bindingResult,
                                          ModelAndView requestContext,
                                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            requestContext.addObject("availableCategories", this.categoryService.loadAll());
            requestContext.addObject("availableAccounts", this.accountService.loadNonDependantAccounts());
            requestContext.addObject("newTransfer", newTransfer);
            requestContext.setViewName("transfers/new.form");
        } else {
            this.transferService.create(newTransfer);
            redirectAttributes.addFlashAttribute("message", "Transfer successfully created.");
            requestContext.setViewName("redirect:/transfers/new");
        }
        return requestContext;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView removeTransfer(@PathVariable("id")Integer id,
                                       @RequestParam(name = "referenceDate", required = true) String referenceDate,
                                       @RequestParam(name = "accountId", required = true) String accountId,
                                       ModelAndView requestContext,
                                       RedirectAttributes redirectAttributes) {
        this.transferService.remove(id);
        redirectAttributes.addFlashAttribute("message", "The transfer was successfully removed.");
        requestContext.setViewName(
            String.format("redirect:/records?referenceDate=%s&accountId=%s", referenceDate, accountId)
        );
        return requestContext;
    }
}