package io.geekmind.budgie.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class StandardAccount extends Account {

    @OneToMany(mappedBy = "parent", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Account> dependants;
}
