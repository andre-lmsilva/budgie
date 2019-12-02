package io.geekmind.budgie.model.entity;

import lombok.Data;
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
@Data
public class BudgetTemplateRecord extends ContainerRecord {

}
