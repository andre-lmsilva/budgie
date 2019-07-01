package io.geekmind.budgie.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Data
@Entity
public abstract class ContainerRecord extends Record {

    @ManyToOne
    @NotNull
    private RecordContainer recordContainer;

}
