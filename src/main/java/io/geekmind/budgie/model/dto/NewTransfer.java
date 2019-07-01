package io.geekmind.budgie.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewTransfer {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate transferDate;

    @NotNull
    private Integer sourceAccountId;

    @NotNull
    private Integer destinationAccountId;

    @NotNull
    private Integer categoryId;

    @NotNull
    @Size(min = 5, max = 140)
    private String description;

    @NotNull
    @Min(0L)
    private BigDecimal transferValue;

}
