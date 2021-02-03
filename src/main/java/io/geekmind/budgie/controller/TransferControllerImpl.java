package io.geekmind.budgie.controller;

import io.geekmind.budgie.model.dto.NewTransfer;
import io.geekmind.budgie.repository.StandardAccountService;
import io.geekmind.budgie.repository.CategoryService;
import io.geekmind.budgie.repository.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@RequestMapping("/transfers")
public class TransferControllerImpl {

    private final CategoryService categoryService;
    private final StandardAccountService standardAccountService;
    private final TransferService transferService;

    @Autowired
    public TransferControllerImpl(CategoryService categoryService,
                                  StandardAccountService standardAccountService,
                                  TransferService transferService) {
        this.categoryService = categoryService;
        this.standardAccountService = standardAccountService;
        this.transferService = transferService;
    }

    @GetMapping("/new")
    public ModelAndView showNewForm(ModelAndView requestContext) {
        requestContext.addObject("availableAccounts", this.standardAccountService.loadAll());
        requestContext.addObject("availableCategories", this.categoryService.loadAll());

        NewTransfer newTransfer = new NewTransfer();
        newTransfer.setTransferDate(LocalDate.now());

        requestContext.addObject("newTransfer", newTransfer);
        requestContext.setViewName("transfers/new.form");
        return requestContext;
    }

    @PostMapping("/create")
    public ModelAndView createNewTransfer(@Valid NewTransfer newTransfer,
                                          BindingResult bindingResult,
                                          ModelAndView requestContext,
                                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            requestContext.addObject("availableAccounts", this.standardAccountService.loadAll());
            requestContext.addObject("availableCategories", this.categoryService.loadAll());
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
