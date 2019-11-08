package io.geekmind.budgie.model.dto.standard_account;

import io.geekmind.budgie.validation.ValidateUniqueness;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * This DTO holds the details to update an existing {@link io.geekmind.budgie.model.entity.Account} entity.
 *
 * @author Andre Silva
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ValidateUniqueness(message = "There is another account with this name.")
public class EditStandardAccount extends BaseStandardAccount {

    @NotNull
    private Integer id;

    @NotNull
    @Size(min = 3, max = 3)
    private String currencyCode;

    private Integer parentId;

}
