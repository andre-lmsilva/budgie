package io.geekmind.budgie.controller;

import io.geekmind.budgie.model.dto.NewInstalment;
import io.geekmind.budgie.repository.StandardAccountService;
import io.geekmind.budgie.repository.CategoryService;
import io.geekmind.budgie.repository.InstalmentContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;

@Controller
@RequestMapping("/instalments")
public class InstalmentControllerImpl {

    private final StandardAccountService standardAccountService;
    private final CategoryService categoryService;
    private final InstalmentContainerService instalmentContainerService;

    @Autowired
    public InstalmentControllerImpl(StandardAccountService standardAccountService,
                                    CategoryService categoryService,
                                    InstalmentContainerService instalmentContainerService) {
        this.standardAccountService = standardAccountService;
        this.categoryService = categoryService;
        this.instalmentContainerService = instalmentContainerService;
    }

    @GetMapping("/new")
    public ModelAndView showNewForm(ModelAndView requestContext) {
        requestContext.addObject("availableAccounts", this.standardAccountService.loadAll());
        requestContext.addObject("availableCategories", this.categoryService.loadAll());
        NewInstalment newInstalment = new NewInstalment();
        newInstalment.setStartingAt(LocalDate.now());
        requestContext.addObject("newInstalment", newInstalment);
        requestContext.setViewName("instalments/new.form");
        return requestContext;
    }

    @PostMapping("/create")
    public ModelAndView createInstalment(@Valid NewInstalment newInstalment,
                                         BindingResult bindingResult,
                                         ModelAndView requestContext,
                                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            requestContext.addObject("newInstalment", newInstalment);
            requestContext.addObject("availableAccounts", this.standardAccountService.loadAll());
            requestContext.addObject("availableCategories", this.categoryService.loadAll());
            requestContext.setViewName("instalments/new.form");
        } else {
            this.instalmentContainerService.create(newInstalment);
            redirectAttributes.addFlashAttribute("message", "Instalments successfully created.");
            requestContext.setViewName("redirect:/instalments/new");
        }

        return requestContext;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView removeTransfer(@PathVariable("id")Integer id,
                                       @RequestParam(name = "referenceDate", required = true) String referenceDate,
                                       @RequestParam(name = "accountId", required = true) String accountId,
                                       ModelAndView requestContext,
                                       RedirectAttributes redirectAttributes) {
        this.instalmentContainerService.remove(id);
        redirectAttributes.addFlashAttribute("message", "All instalments were successfully removed.");
        requestContext.setViewName(
            String.format("redirect:/records?referenceDate=%s&accountId=%s", referenceDate, accountId)
        );
        return requestContext;
    }
}
