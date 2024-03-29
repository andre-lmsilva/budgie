package io.geekmind.budgie.model.dto.budget_template_record;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * DTO used to hold the details about a new {@link io.geekmind.budgie.model.entity.BudgetTemplateRecord} being created.
 *
 * @author Andre Silva
 */
@Data
public class NewBudgetTemplateRecord {

    @NotNull
    @Min(1)
    @Max(31)
    private Integer dayOfMonth;

    @NotNull
    private Integer accountId;

    @NotNull
    private Integer categoryId;

    @NotNull
    @Size(min = 5, max = 140)
    private String description;

    @NotNull
    private BigDecimal recordValue;

    @NotNull
    private Boolean isTaxRefundable = false;

}
