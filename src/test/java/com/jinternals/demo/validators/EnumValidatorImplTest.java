package com.jinternals.demo.validators;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class EnumValidatorImplTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldNotFailIfValueIsCaseSensitive() {
        DemoClass1 demoClass = new DemoClass1("DEMO_ENUM_1");

        Set<ConstraintViolation<DemoClass1>> constraintViolations = validator.validate(demoClass);

        assertThat(constraintViolations.size()).isZero();
    }

    @Test
    void shouldFailIfValueIsNotCaseSensitive() {
        DemoClass1 demoClass = new DemoClass1("demo_enum_1");

        Set<ConstraintViolation<DemoClass1>> constraintViolations = validator.validate(demoClass);

        assertThat(constraintViolations.size()).isOne();
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo("'demo_enum_1' is not valid type.");
    }

    @Test
    void shouldNotFailIfValueIsNotCaseSensitive() {
        DemoClass2 demoClass = new DemoClass2("demo_enum_2");

        Set<ConstraintViolation<DemoClass2>> constraintViolations = validator.validate(demoClass);

        assertThat(constraintViolations.size()).isZero();
    }


    enum DemoEnum {
        DEMO_ENUM_1, DEMO_ENUM_2
    }

    static class DemoClass1 {

        public DemoClass1(String value) {
            this.value = value;
        }

        @EnumValidator(
                enumClass = DemoEnum.class,
                caseSensitive = true,
                message = "'${validatedValue}' is not valid type."
        )
        private String value;

    }

    static class DemoClass2 {

        public DemoClass2(String value) {
            this.value = value;
        }

        @EnumValidator(
                enumClass = DemoEnum.class,
                caseSensitive = false,
                message = "'${validatedValue}' is not valid type."
        )
        private String value;

    }

}
