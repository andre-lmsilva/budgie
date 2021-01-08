package io.geekmind.budgie.model.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "record_type")
public abstract class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @ManyToOne
    private Account account;

    @NotNull
    @ManyToOne
    private Category category;

    @NotNull
    private LocalDate recordDate;

    @NotNull
    @Size(min = 5, max = 140)
    @Type(type = "encryptedTextField")
    private String description;

    @NotNull
    @Type(type = "encryptedDecimalField")
    private BigDecimal recordValue;

    @Size(max = 140)
    @Type(type = "encryptedTextField")
    private String bankStatementId;

    @OneToMany(mappedBy = "record", orphanRemoval = true, cascade = {CascadeType.ALL})
    private List<Attachment> attachments;

}
