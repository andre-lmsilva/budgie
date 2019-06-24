package io.geekmind.budgie.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EntityUniquenessValidator.class)
@Documented
public @interface ValidateUniqueness {

    String message() default "has already been taken.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
