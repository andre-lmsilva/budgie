package io.geekmind.budgie.model.dto;

import lombok.Data;

@Data
public class ExistingAccount {

    private Integer id;

    private String name;

    private String description;

    private Integer monthStartingAt;

    private Integer monthBillingDayAt;

    private Boolean mainAccount;

    private Boolean showBalanceOnMainAccount;

}
