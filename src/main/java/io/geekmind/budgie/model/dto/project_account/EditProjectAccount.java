package io.geekmind.budgie.model.dto.project_account;

import io.geekmind.budgie.validation.ValidateUniqueness;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@ValidateUniqueness(message = "There is another project with this same name.")
public class EditProjectAccount extends BaseProjectAccount {

    @NotNull
    private Integer id;

}
