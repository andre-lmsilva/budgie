package io.geekmind.budgie.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

/**
 * Container that groups all the budget records created based on a {@link BudgetTemplateRecord}.
 *
 * @author Andre Silva
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class BudgetTemplateRecordContainer extends RecordContainer {
}
