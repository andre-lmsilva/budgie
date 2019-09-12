package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.dto.BalanceSummary;
import io.geekmind.budgie.model.dto.CategoryBalanceSummary;
import io.geekmind.budgie.model.dto.ExistingCategory;
import io.geekmind.budgie.model.dto.ExistingRecord;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class StandardBalanceServiceCalculateCategoryBalanceSummaryTest {

    private StandardBalanceService balanceService = new StandardBalanceService(null, null, null);
    private ExistingCategory fakeDwelling;
    private ExistingCategory fakeInsurance;
    private ExistingCategory fakeSalary;
    private BalanceSummary fakeBalanceSummary;
    private List<CategoryBalanceSummary> result;

    @Before
    public void setUp() {
        this.fakeDwelling = new ExistingCategory(99, "Dwelling", "", BigDecimal.ZERO);
        ExistingRecord fakeDwellingRecord = new ExistingRecord(99, fakeDwelling, null, LocalDate.now(), "Monthly Rent", BigDecimal.valueOf(-150D), null, null);

        this.fakeInsurance = new ExistingCategory(100, "Insurance", "", BigDecimal.ZERO);
        ExistingRecord fakeCarInsurance = new ExistingRecord(100, fakeInsurance, null, LocalDate.now(), "Car Insurance", BigDecimal.valueOf(-300D), null, null);

        this.fakeSalary = new ExistingCategory(199, "Salary", "", BigDecimal.ZERO);
        ExistingRecord fakeMonthlySalary = new ExistingRecord(199, fakeSalary, null, LocalDate.now(), "Monthly Salary", BigDecimal.valueOf(1200D), null, null);

        this.fakeBalanceSummary = new BalanceSummary(
            BigDecimal.valueOf(750D),
            BigDecimal.valueOf(1200D),
            BigDecimal.valueOf(-450D),
            BigDecimal.valueOf(750D)
        );

        this.result = this.balanceService.calculateCategoryBalanceSummary(
            Arrays.asList(fakeMonthlySalary, fakeDwellingRecord, fakeCarInsurance),
            this.fakeBalanceSummary
        );
    }

    @Test
    public void resultHasOnlyTwoItems() {
        assertThat(this.result).hasSize(2);
    }

    @Test
    public void resultIsSortedByExpenseCompromisePercentage() {
        assertThat(
            this.result.stream().map(CategoryBalanceSummary::getCategory).collect(Collectors.toList())
        ).containsExactly(this.fakeInsurance, this.fakeDwelling);
    }

    @Test
    public void resultCalculatedTheCorrectPercentages() {
        assertThat(
            this.result.stream()
                .map(balanceSummary -> balanceSummary.getExpensesConsumptionPercentage().doubleValue())
                .collect(Collectors.toList())
        ).containsExactly(70D, 30D);
    }

}
