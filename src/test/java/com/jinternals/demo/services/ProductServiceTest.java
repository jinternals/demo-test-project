package com.jinternals.demo.services;


import com.jinternals.demo.domain.Product;
import com.jinternals.demo.domain.events.ProductCreatedEvent;
import com.jinternals.demo.events.EventGateway;
import com.jinternals.demo.exceptions.ProductNotFoundException;
import com.jinternals.demo.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;

import javax.validation.ConstraintViolationException;

import static com.jinternals.demo.domain.Product.builder;
import static com.jinternals.demo.domain.ProductType.FOOD;
import static com.jinternals.demo.test.support.ValidationTestUtil.validationProxy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static reactor.core.publisher.Mono.just;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private EventGateway eventGateway;

    @Mock
    private IdGenerator idGenerator;

    private ProductService productService;

    @BeforeEach
    void initializeTest() {
        productService = validationProxy(new ProductService(productRepository, eventGateway, idGenerator));
    }

    @Test
    void shouldSaveTheProduct() {
        Product product = builder().name("Apple").type(FOOD).build();
        when(productRepository.save(product)).thenReturn(just(product));
        ProductCreatedEvent productCreatedEvent = ProductCreatedEvent.builder()
                .id("some-id-1")
                .name("Apple")
                .type(FOOD)
                .build();
        when(eventGateway.publish(productCreatedEvent)).thenReturn(just(mock(SenderResult.class)));
        when(idGenerator.generateId()).thenReturn("some-id-1");

        Product savedProduct = productService.createProduct(product).block();

        verify(productRepository).save(product);
        verify(eventGateway).publish(productCreatedEvent);
        assertThat(savedProduct).isEqualTo(product);
    }

    @Test
    void shouldFindProductByType() {
        when(productRepository.findProductByType(FOOD)).thenReturn(Flux.just(
                builder().id("some-id-1").name("Apple").type(FOOD).build(),
                builder().id("some-id-2").name("Banana").type(FOOD).build()
        ));

        create(productService.findProductByType(FOOD))
                .expectNextMatches(product -> product.equals(builder().id("some-id-1").name("Apple").type(FOOD).build()))
                .expectNextMatches(product -> product.equals(builder().id("some-id-2").name("Banana").type(FOOD).build()))
                .verifyComplete();

        verify(productRepository).findProductByType(FOOD);
    }

    @Test
    void shouldGetProductById() {
        Product product = builder().id("some-id-1").name("Apple").type(FOOD).build();
        when(productRepository.findById("some-id-1")).thenReturn(just(product));

        Product savedProduct = productService.getProductById("some-id-1").block();

        verify(productRepository).findById("some-id-1");

        assertThat(savedProduct).isEqualTo(product);
    }

    @Test
    void shouldThrowProductNotFoundException() {
        when(productRepository.findById("some-id-1")).thenReturn(Mono.empty());

        assertThatThrownBy(() -> productService.getProductById("some-id-1").block(), "")
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Product with id some-id-1 not found.");

        verify(productRepository).findById("some-id-1");
    }

    @Test
    void shouldThrowConstraintViolationExceptionForCreateProduct() {
        Product product = builder().build();

        assertThatThrownBy(() -> productService.createProduct(product))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(
                        "Empty or null name is not allowed",
                        "null Empty is not allowed"
                );

    }

    @Test
    void shouldThrowConstraintViolationExceptionForFindProductByType() {

        assertThatThrownBy(() -> productService.findProductByType(null))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("productType should not be null");

    }

    @Test
    void shouldThrowConstraintViolationExceptionForGetProductById() {

        assertThatThrownBy(() -> productService.getProductById(""))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("productId should not be null or empty");

        assertThatThrownBy(() -> productService.getProductById(null))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("productId should not be null or empty");

    }
}
