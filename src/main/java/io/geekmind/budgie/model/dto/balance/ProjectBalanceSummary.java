package io.geekmind.budgie.model.dto.balance;

import io.geekmind.budgie.model.dto.project_account.ExistingProjectAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectBalanceSummary {

    private ExistingProjectAccount project;

    private BigDecimal balance;

    private BigDecimal progressPercentage;

}
