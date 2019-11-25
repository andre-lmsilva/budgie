package io.geekmind.budgie.controller;

import io.geekmind.budgie.model.dto.balance.BalanceType;
import io.geekmind.budgie.model.dto.NewSingleRecord;
import io.geekmind.budgie.repository.StandardAccountService;
import io.geekmind.budgie.repository.CategoryService;
import io.geekmind.budgie.repository.SingleRecordService;
import io.geekmind.budgie.repository.StandardBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@RequestMapping("/single_records")
public class SingleRecordControllerImpl {

    private final StandardAccountService standardAccountService;
    private final CategoryService categoryService;
    private final SingleRecordService singleRecordService;
    private final StandardBalanceService balanceService;

    private DateTimeFormatter dateFormatter;

    @Autowired
    public SingleRecordControllerImpl(StandardAccountService standardAccountService,
                                      CategoryService categoryService,
                                      SingleRecordService singleRecordService,
                                      StandardBalanceService balanceService) {
        this.standardAccountService = standardAccountService;
        this.categoryService = categoryService;
        this.singleRecordService = singleRecordService;
        this.balanceService = balanceService;
    }

    @PostConstruct
    public void initialize() {
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    @GetMapping("/new")
    public ModelAndView showNewSingleRecordForm(@RequestParam(value = "recordDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordDate,
                                                @RequestParam(value = "accountId", required = false) Integer accountId,
                                                @RequestParam(value = "recordDescription", required = false) String recordDescription,
                                                @RequestParam(value = "recordValue", required = false) BigDecimal recordValue,
                                                ModelAndView requestContext) {
        NewSingleRecord newSingleRecord = new NewSingleRecord(
            Optional.ofNullable(recordDate).orElse(LocalDate.now()),
            accountId,
            null,
            recordDescription,
            recordValue,
            null
        );
        requestContext.addObject("newSingleRecord", newSingleRecord);
        requestContext.addObject("balance", this.balanceService.generateBalance(accountId, recordDate, BalanceType.REGULAR_PERIOD_BALANCE));
        requestContext.addObject("availableAccounts", this.standardAccountService.loadAll());
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
            requestContext.addObject("availableAccounts", this.standardAccountService.loadAll());
            requestContext.addObject("availableCategories", this.categoryService.loadAll());
            requestContext.setViewName("single_records/new.form");
        } else {
            this.singleRecordService.create(newSingleRecord);
            redirectAttributes.addFlashAttribute("message", "Single record successfully created!");
            String recordDate = this.dateFormatter.format(newSingleRecord.getRecordDate());

            requestContext.setViewName(
                String.format("redirect:/single_records/new?recordDate=%s&accountId=%s", recordDate, newSingleRecord.getAccountId())
            );
        }
        return requestContext;
    }

}
