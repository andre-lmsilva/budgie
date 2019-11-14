package io.geekmind.budgie.model.dto.project_account;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BaseProjectAccount implements Serializable {

    @NotNull
    @Size(min = 5, max = 140)
    private String name;

    private String description;

    @NotNull
    private Integer parentId;

    @NotNull
    @Min(1)
    private BigDecimal targetValue;

}
