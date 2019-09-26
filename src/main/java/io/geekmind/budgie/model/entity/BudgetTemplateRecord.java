package io.geekmind.budgie.model.entity;

import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

/**
 * Holds the details of a budget record template that can be replicated across periods.
 *
 * @author Andre Silva
 */
@Entity
@EqualsAndHashCode(callSuper = true)
public class BudgetTemplateRecord extends ContainerRecord {
}
