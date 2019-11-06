package io.geekmind.budgie.model.dto.category;

import io.geekmind.budgie.validation.ValidateUniqueness;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@ValidateUniqueness(message = "There is another category with this same name.")
public class EditCategory extends BaseCategory {

    @NotNull
    private Integer id;

}
