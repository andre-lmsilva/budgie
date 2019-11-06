package io.geekmind.budgie.model.dto.category;

import io.geekmind.budgie.validation.ValidateUniqueness;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Holds the details to create a new category.
 *
 * @author Andre Silva
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ValidateUniqueness(message = "There is another category with this name already.")
public class NewCategory extends BaseCategory {
}
