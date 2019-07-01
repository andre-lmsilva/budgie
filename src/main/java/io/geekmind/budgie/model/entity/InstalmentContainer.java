package io.geekmind.budgie.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import java.util.ArrayList;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class InstalmentContainer extends RecordContainer {

    public void addRecord(ContainerRecord record) {
        if (null == this.getRecords()) {
            this.setRecords(new ArrayList<>());
        }
        record.setRecordContainer(this);
        this.getRecords().add(record);
    }

}
