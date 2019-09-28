package io.geekmind.budgie.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

/**
 * Holds the information about a record create based on a {@link BudgetTemplateRecord}.
 *
 * @author Andre Silva
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class BudgetRecord extends ContainerRecord {
}
