package io.geekmind.budgie.model.dto.category;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Holds the common attributes shared by the DTOs related with category management operations.
 *
 * @author Andre Silva
 */
@Data
public class BaseCategory {

    @NotNull
    @Size(min = 5, max = 140)
    private String name;

    private String description;

    private BigDecimal maxExpenses;

}
