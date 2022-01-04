package com.jinternals.demo.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.jinternals.demo.domain.ProductType.FOOD;

class ProductTest {

    @Test
    void shouldVerifyProductAreEqual() {

        Product product1 = Product
                .builder()
                .id("some-id")
                .name("some-product-name")
                .description("some-product-description")
                .type(FOOD)
                .build();

        Product product2 = Product
                .builder()
                .id("some-id")
                .name("some-product-name")
                .description("some-product-description")
                .type(FOOD)
                .build();

        Assertions.assertThat(product1).isEqualTo(product2);
    }

    @Test
    void shouldVerifyProductAreNotEqual() {

        Product product1 = Product
                .builder()
                .id("some-id-1")
                .name("some-product-name")
                .description("some-product-description")
                .type(FOOD)
                .build();

        Product product2 = Product
                .builder()
                .id("some-id-2")
                .name("some-product-name")
                .description("some-product-description")
                .type(FOOD)
                .build();

        Assertions.assertThat(product1).isNotEqualTo(product2);
    }

}
