package io.geekmind.budgie.model.dto;

import io.geekmind.budgie.validation.ValidateUniqueness;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidateUniqueness(message = "There is another account with this name already.")
public class NewAccount {

    @NotNull
    @Size(min = 5, max = 255)
    private String name;

    private String description;

    @NotNull
    @Min(1)
    @Max(31)
    private Integer monthStartingAt;

    @NotNull
    @Min(1)
    @Max(31)
    private Integer monthBillingDayAt;

    private Boolean showBalanceOnMainAccount;

}
