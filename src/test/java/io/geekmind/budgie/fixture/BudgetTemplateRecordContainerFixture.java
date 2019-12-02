package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.entity.BudgetTemplateRecordContainer;

public class BudgetTemplateRecordContainerFixture {

    public static BudgetTemplateRecordContainer rentContainer() {
        BudgetTemplateRecordContainer container = new BudgetTemplateRecordContainer();
        container.setId(-1);
        container.setName("Fake Budget Template Record Container for Test Purposes.");
        container.addRecord(BudgetTemplateRecordFixture.rent());
        return container;
    }

}
