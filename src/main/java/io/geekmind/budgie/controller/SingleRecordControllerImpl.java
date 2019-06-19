package io.geekmind.budgie.controller;

import io.geekmind.budgie.model.dto.NewSingleRecord;
import io.geekmind.budgie.repository.AccountService;
import io.geekmind.budgie.repository.CategoryService;
import io.geekmind.budgie.repository.SingleRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Controller
@RequestMapping("/single_records")
public class SingleRecordControllerImpl {

    private final AccountService accountService;
    private final CategoryService categoryService;
    private final SingleRecordService singleRecordService;

    @Autowired
    public SingleRecordControllerImpl(AccountService accountService,
                                      CategoryService categoryService,
                                      SingleRecordService singleRecordService) {
        this.accountService = accountService;
        this.categoryService = categoryService;
        this.singleRecordService = singleRecordService;
    }

    @GetMapping("/new")
    public ModelAndView showNewSingleRecordForm(ModelAndView requestContext) {
        requestContext.addObject("newSingleRecord", new NewSingleRecord());
        requestContext.addObject("availableAccounts", this.accountService.loadAll());
        requestContext.addObject("availableCategories", this.categoryService.loadAll());
        requestContext.setViewName("single_records/new.form");
        return requestContext;
    }

    @PostMapping("/create")
    public ModelAndView createNewSingleRecord(@Valid NewSingleRecord newSingleRecord,
                                              BindingResult bindingResult,
                                              ModelAndView requestContext,
                                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            requestContext.addObject("newSingleRecord", newSingleRecord);
            requestContext.addObject("availableAccounts", this.accountService.loadAll());
            requestContext.addObject("availableCategories", this.categoryService.loadAll());
            requestContext.setViewName("single_records/new.form");
        } else {
            this.singleRecordService.create(newSingleRecord);
            redirectAttributes.addFlashAttribute("message", "Single record successfully created!");
            requestContext.setViewName("redirect:/single_records/new");
        }
        return requestContext;
    }

}
