package io.geekmind.budgie.validation;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class EntityUniquenessValidator implements ConstraintValidator<ValidateUniqueness, Object> {

    @Autowired
    private List<UniquenessValidationService> validationServices;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        return this.validationServices.stream()
            .filter(service -> service.canValidate(value.getClass()))
            .findFirst()
            .map(service -> service.isValid(value))
            .orElseThrow(() -> {
                String message = String.format("No suitable service found to validate the uniqueness of %s.", value.getClass().getName());
                return new RuntimeException(message);
            });
    }
}
