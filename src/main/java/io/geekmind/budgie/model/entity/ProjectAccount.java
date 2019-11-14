package io.geekmind.budgie.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * A project account is an specialization of an account used to keep track of saving done to achieve a certain purpose.
 *
 * @author Andre Silva
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectAccount extends Account {

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private StandardAccount parent;

    @NotNull
    @Min(1)
    @Type(type = "encryptedDecimalField")
    private BigDecimal targetValue;
}
