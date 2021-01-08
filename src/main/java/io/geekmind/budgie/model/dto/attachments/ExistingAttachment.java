package io.geekmind.budgie.model.dto.attachments;

import io.geekmind.budgie.model.entity.Attachment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExistingAttachment {

    private Integer id;

    private Integer recordId;

    private String attachmentName;

    private String mimeType;

    private BigDecimal sizeKb;

}
