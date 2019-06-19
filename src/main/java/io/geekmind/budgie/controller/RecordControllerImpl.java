package io.geekmind.budgie.controller;

import io.geekmind.budgie.repository.AccountService;
import io.geekmind.budgie.repository.BalanceService;
import io.geekmind.budgie.repository.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/records")
public class RecordControllerImpl {

    private final BalanceService balanceService;
    private final AccountService accountService;
    private final RecordService recordService;

    @Autowired
    public RecordControllerImpl(BalanceService balanceService,
                                AccountService accountService,
                                RecordService recordService) {
        this.balanceService = balanceService;
        this.accountService = accountService;
        this.recordService = recordService;
    }

    @GetMapping
    public ModelAndView loadBalance(@RequestParam(name = "referenceDate", required = false)
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate referenceDate,
                                    @RequestParam(name = "accountId", required = false) Integer accountId,
                                    ModelAndView requestContext) {
        requestContext.addObject("balance", this.balanceService.generateBalance(accountId, referenceDate));
        requestContext.addObject("availableAccounts", this.accountService.loadAll());
        requestContext.setViewName("records/index");
        return requestContext;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView removeRecord(@PathVariable("id")Integer id,
                                     @RequestParam(name = "referenceDate", required = true) String referenceDate,
                                     @RequestParam(name = "accountId", required = true) String accountId,
                                     ModelAndView requestContext,
                                     RedirectAttributes redirectAttributes) {
        this.recordService.remove(id);
        redirectAttributes.addFlashAttribute("message", "The record was successfully removed.");
        requestContext.setViewName(
            String.format("redirect:/records?referenceDate=%s&accountId=%s", referenceDate, accountId)
        );
        return requestContext;
    }

}
