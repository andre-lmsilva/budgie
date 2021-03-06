package io.geekmind.budgie.model.dto;

import io.geekmind.budgie.model.dto.attachments.ExistingAttachment;
import io.geekmind.budgie.model.dto.standard_account.ExistingStandardAccount;
import io.geekmind.budgie.model.dto.category.ExistingCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExistingRecord implements Serializable {

    @NotNull
    private Integer id;

    @NotNull
    private ExistingCategory category;

    @NotNull
    private ExistingStandardAccount account;

    private List<ExistingAttachment> attachments;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate recordDate;

    @NotNull
    @Size(min = 5, max = 140)
    private String description;

    @NotNull
    private BigDecimal recordValue;

    private String recordType;

    private Integer containerId;

    @Size(max = 140)
    private String bankStatementId;

    private Boolean upcoming = Boolean.FALSE;

    private Boolean lastCreated = Boolean.FALSE;

    private Boolean isTaxRefundable = Boolean.FALSE;

}
