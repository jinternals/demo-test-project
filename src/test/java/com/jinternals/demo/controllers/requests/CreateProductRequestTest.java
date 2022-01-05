package com.jinternals.demo.controllers.requests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CreateProductRequestTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldValidateTypeIsValid() {
        CreateProductRequest request = CreateProductRequest.builder().name("some-name").type("food").build();

        Set<ConstraintViolation<CreateProductRequest>> constraintViolations = validator.validate( request );

        assertThat(constraintViolations.size()).isOne();
        assertThat( constraintViolations.iterator().next().getMessage()).isEqualTo("'food' is not valid type.");
    }


    @Test
    public void shouldValidateNameIsNotNull() {
        CreateProductRequest request = CreateProductRequest.builder().type("FOOD").build();

        Set<ConstraintViolation<CreateProductRequest>> constraintViolations = validator.validate( request );

        assertThat(constraintViolations.size()).isOne();
        assertThat( constraintViolations.iterator().next().getMessage()).isEqualTo("Empty or null name not allowed");

    }


    @Test
    public void shouldNotFailValidationIfAllTheValuesAreCorrect() {
        CreateProductRequest request = CreateProductRequest.builder().name("some-name").type("FOOD").build();

        Set<ConstraintViolation<CreateProductRequest>> constraintViolations = validator.validate( request );

        assertThat(constraintViolations.size()).isZero();
    }
}
