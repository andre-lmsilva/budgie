package io.geekmind.budgie.model.dto.category;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExistingCategory extends BaseCategory {

    private Integer id;

}
