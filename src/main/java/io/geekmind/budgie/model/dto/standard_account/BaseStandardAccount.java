package io.geekmind.budgie.model.dto.standard_account;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Defines the basic common attributes shared by the DTOs that can be mapped from/to the {@link io.geekmind.budgie.model.entity.Account}
 * entity.
 *
 * @author Andre Silva
 */
@Data
public class BaseStandardAccount implements Serializable {

    @NotNull
    @Size(min = 5, max = 140)
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

    private Boolean showBalanceOnParentAccount;

}
