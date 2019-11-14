package io.geekmind.budgie.model.dto.project_account;

import io.geekmind.budgie.validation.ValidateUniqueness;

@ValidateUniqueness(message = "There is another project with this same name already.")
public class NewProjectAccount extends BaseProjectAccount {

}
