package io.geekmind.budgie.model.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Account parent;

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
    private Boolean showBalanceOnParentAccount;

    @NotNull
    @Type(type = "encryptedTextField")
    private String currencyCode;

    @NotNull
    private Boolean archived = Boolean.FALSE;

}

