package com.jinternals.demo.validators;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, String> {

    List<String> valueList = null;
    private EnumValidator enumValidator;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean caseSensitive = enumValidator.caseSensitive();
        return valueList
                .stream()
                .anyMatch(s -> caseSensitive? s.equals(value): s.equalsIgnoreCase(value));
    }

    @Override
    public void initialize(EnumValidator enumValidator) {
        this.enumValidator = enumValidator;
        valueList = new ArrayList<>();

        Class<? extends Enum<?>> enumClass = enumValidator.enumClass();

        for (@SuppressWarnings("rawtypes") Enum value : enumClass.getEnumConstants()) {
            valueList.add(value.toString());
        }
    }

}
