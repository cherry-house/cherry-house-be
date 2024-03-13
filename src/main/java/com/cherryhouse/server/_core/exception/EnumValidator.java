package com.cherryhouse.server._core.exception;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<EnumValid, Enum<?>> {

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        return value != null && !value.toString().trim().isEmpty();
    }
}
