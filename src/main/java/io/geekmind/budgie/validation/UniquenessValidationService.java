package io.geekmind.budgie.validation;

public interface UniquenessValidationService {

    Boolean canValidate(Class<?> type);

    Boolean isValid(Object entity);

}
