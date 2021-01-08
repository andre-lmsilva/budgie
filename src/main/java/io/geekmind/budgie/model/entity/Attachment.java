package io.geekmind.budgie.model.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Entity
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Record record;

    @Column(nullable = false)
    @Type(type = "encryptedTextField")
    @NotBlank
    private String attachmentName;

    @Column(name = "mimetype", nullable = false)
    @Type(type = "encryptedTextField")
    @NotBlank
    private String mimeType;

    @Column(nullable = false)
    @Type(type = "encryptedDecimalField")
    @NotNull
    private BigDecimal sizeKb;

}
