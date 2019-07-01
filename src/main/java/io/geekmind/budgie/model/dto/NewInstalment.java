package io.geekmind.budgie.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewInstalment {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startingAt;

    @NotNull
    private Integer accountId;

    @NotNull
    private Integer categoryId;

    @NotNull
    @Size(min = 5, max = 140)
    private String description;

    @NotNull
    @Min(2)
    @Max(120)
    private Integer numberOfInstalments;

    @NotNull
    private BigDecimal instalmentValue;

}
