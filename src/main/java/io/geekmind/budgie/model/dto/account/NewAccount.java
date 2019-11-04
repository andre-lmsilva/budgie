package io.geekmind.budgie.model.dto.account;

import io.geekmind.budgie.validation.ValidateUniqueness;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * This DTO holds the details required to create a new {@link io.geekmind.budgie.model.entity.Account} entity.
 *
 * @author Andre Silva
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ValidateUniqueness(message = "There is another account with this name already.")
public class NewAccount extends BaseAccount {

    @NotNull
    private Integer id;

    @NotNull
    @Size(min = 3, max = 3)
    private String currencyCode;

}
