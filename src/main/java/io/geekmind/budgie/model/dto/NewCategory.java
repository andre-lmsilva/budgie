package io.geekmind.budgie.model.dto;

import io.geekmind.budgie.validation.ValidateUniqueness;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidateUniqueness(message = "There is another category with this name already.")
public class NewCategory {

    @NotNull
    @Size(min = 5, max = 140)
    private String name;

    private String description;

}
