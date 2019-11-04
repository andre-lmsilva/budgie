package io.geekmind.budgie.model.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(min = 5, max = 255)
    @Type(type = "encryptedTextField")
    private String name;

    @Type(type = "encryptedTextField")
    private String description;

    @NotNull
    @Min(1)
    @Max(31)
    private Integer monthStartingAt;

    @NotNull
    @Min(1)
    @Max(31)
    private Integer monthBillingDayAt;

    @NotNull
    private Boolean mainAccount = Boolean.FALSE;

    @NotNull
    private Boolean showBalanceOnMainAccount;

    @NotNull
    @Type(type = "encryptedTextField")
    private String currencyCode;

}
