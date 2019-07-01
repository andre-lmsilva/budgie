package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.entity.InstalmentContainer;


public class InstalmentContainerFixture {

    public static InstalmentContainer get() {
        InstalmentContainer container = new InstalmentContainer();
        container.setId(-1);
        container.setName("Fake Instalment");
        container.addRecord(InstalmentRecordFixture.getWithoutInstalmentContainer());
        return container;
    }

}
