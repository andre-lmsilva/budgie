package io.geekmind.budgie.controller.report;

import io.geekmind.budgie.model.dto.report.TaxRefundableReportRequest;
import io.geekmind.budgie.repository.StandardAccountService;
import io.geekmind.budgie.repository.TaxRefundableReportService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

@Controller
@RequestMapping("/report/tax_refundable")
public class TaxRefundableReportController {

    private final StandardAccountService standardAccountService;
    private final TaxRefundableReportService taxRefundableReportService;

    public TaxRefundableReportController(StandardAccountService standardAccountService,
                                         TaxRefundableReportService taxRefundableReportService) {
        this.standardAccountService = standardAccountService;
        this.taxRefundableReportService = taxRefundableReportService;
    }

    @GetMapping
    public ModelAndView index(ModelAndView modelAndView) {
        TaxRefundableReportRequest request = new TaxRefundableReportRequest();
        request.setExistingAccountId(this.standardAccountService.getMainAccount().get().getId());
        request.setStartDate(LocalDate.of(LocalDate.now().getYear(), 1, 1));
        request.setEndDate(LocalDate.of(LocalDate.now().getYear(), 12, 31));

        modelAndView.addObject("availableAccounts", this.standardAccountService.loadAll());
        modelAndView.addObject("request", request);
        modelAndView.addObject("summaries", this.taxRefundableReportService.generateTaxRefundableSummaries(request));
        modelAndView.setViewName("taxRefundable/index");
        return modelAndView;
    }

}
