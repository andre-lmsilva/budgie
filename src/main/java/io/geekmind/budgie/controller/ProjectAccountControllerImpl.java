package io.geekmind.budgie.controller;

import io.geekmind.budgie.model.dto.project_account.EditProjectAccount;
import io.geekmind.budgie.model.dto.project_account.NewProjectAccount;
import io.geekmind.budgie.repository.ProjectAccountService;
import io.geekmind.budgie.repository.StandardAccountService;
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
@RequestMapping("/project_accounts")
public class ProjectAccountControllerImpl {

    private final StandardAccountService standardAccountService;
    private final ProjectAccountService projectAccountService;

    public ProjectAccountControllerImpl(StandardAccountService standardAccountService,
                                        ProjectAccountService projectAccountService) {
        this.standardAccountService = standardAccountService;
        this.projectAccountService = projectAccountService;
    }

    @GetMapping
    public ModelAndView loadAll(ModelAndView requestContext) {
        requestContext.addObject("availableProjectAccounts", this.projectAccountService.loadAll());
        requestContext.setViewName("project_accounts/index");
        return requestContext;
    }

    @GetMapping("/new")
    public ModelAndView showNewForm(ModelAndView requestContext) {
        requestContext.addObject("availableAccounts", standardAccountService.loadAll());
        requestContext.addObject("newProjectAccount", new NewProjectAccount());
        requestContext.setViewName("project_accounts/new.form");
        return requestContext;
    }

    @PostMapping("/create")
    public ModelAndView createNewProjectAccount(@Valid NewProjectAccount newProjectAccount,
                                                ModelAndView requestContext,
                                                RedirectAttributes redirectAttributes,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            requestContext.addObject("availableAccounts", standardAccountService.loadAll());
            requestContext.addObject("newProjectAccount", newProjectAccount);
            requestContext.setViewName("project_accounts/new.form");
        } else {
            this.projectAccountService.create(newProjectAccount);
            redirectAttributes.addFlashAttribute("message", "Project successfully created!");
            requestContext.setViewName("redirect:/project_accounts/new");
        }
        return requestContext;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable("id") Integer id,
                                     ModelAndView requestContext,
                                     RedirectAttributes redirectAttributes) {

        Optional<EditProjectAccount> editProjectAccount = this.projectAccountService.loadByIdForEdit(id);
        if (editProjectAccount.isPresent()) {
            requestContext.addObject("editProjectAccount", editProjectAccount.get());
            requestContext.addObject("availableAccounts", this.standardAccountService.loadAll());
            requestContext.setViewName("project_accounts/edit.form");
        } else {
            redirectAttributes.addFlashAttribute("error", "Project not found.");
            requestContext.setViewName("redirect:/project_accounts");
        }
        return requestContext;
    }

    @PostMapping("/update")
    public ModelAndView updateProjectAccount(@Valid EditProjectAccount editProjectAccount,
                                             ModelAndView requestContext,
                                             BindingResult bindingResult,
                                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            requestContext.addObject("editProjectAccount", editProjectAccount);
            requestContext.addObject("availableAccounts", this.standardAccountService.loadAll());
            requestContext.setViewName("project_accounts/edit.form");
        } else {
            this.projectAccountService.update(editProjectAccount);
            redirectAttributes.addFlashAttribute("message", "The project was successfully updated.");
            requestContext.setViewName("redirect:/project_accounts");
        }
        return requestContext;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteProjectAccount(@PathVariable("id") Integer id,
                                             ModelAndView requestContext,
                                             RedirectAttributes redirectAttributes) {
        if (this.projectAccountService.delete(id).isPresent()) {
            redirectAttributes.addFlashAttribute("message", "Project successfully archived.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Project not found.");
        }
        requestContext.setViewName("redirect:/project_accounts");
        return requestContext;
    }

}
