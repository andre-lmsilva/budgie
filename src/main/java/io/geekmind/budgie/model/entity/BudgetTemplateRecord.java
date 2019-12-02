package io.geekmind.budgie.model.entity;

import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Holds the details of a budget record template that can be replicated across periods.
 *
 * @author Andre Silva
 */
@Entity
@EqualsAndHashCode(callSuper = true)
public class BudgetTemplateRecord extends ContainerRecord {

    @NotNull
    @ManyToOne
    private StandardAccount account;

}
