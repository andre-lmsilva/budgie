package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.entity.TransferContainer;

public class TransferContainerFixture {

    public static TransferContainer get() {
        TransferContainer container = new TransferContainer();
        container.setId(-100);
        container.setName("Fake Transfer Container");
        return container;
    }

}
