package io.geekmind.budgie.fixture;

import io.geekmind.budgie.model.dto.project_account.EditProjectAccount;

import java.math.BigDecimal;

public class EditProjectAccountFixture {

    public static EditProjectAccount macBookPro() {
        EditProjectAccount editProjectAccount = new EditProjectAccount();
        editProjectAccount.setId(-999);
        editProjectAccount.setName("Buy a new MacBook Pro");
        editProjectAccount.setDescription("This project aims to save enough money to buy a new macbook pro.");
        editProjectAccount.setParentId(-666);
        editProjectAccount.setTargetValue(BigDecimal.valueOf(123.45D));
        return editProjectAccount;
    }

}
