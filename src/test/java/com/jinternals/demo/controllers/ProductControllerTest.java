package com.jinternals.demo.controllers;

import com.jinternals.demo.controllers.requests.CreateProductRequest;
import com.jinternals.demo.domain.Product;
import com.jinternals.demo.exceptions.ProductNotFoundException;
import com.jinternals.demo.services.IdGenerator;
import com.jinternals.demo.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.jinternals.demo.domain.ProductType.FOOD;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    private ProductService productService;

    @MockBean
    private IdGenerator idGenerator;

    @Test
    void shouldCreateProduct() {

        CreateProductRequest request = CreateProductRequest.builder().name("some-product").type("FOOD").build();

        Product product = Product.builder().id("some-id").name("some-product").type(FOOD).build();

        Mono<Product> productMono = Mono.just(product);

        when(productService.createProduct(product)).thenReturn(productMono);
        when(idGenerator.generateId()).thenReturn("some-id");

        webTestClient.post()
                .uri("/api/product")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateProductRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Product.class)
                .value(Product::getId, equalTo("some-id"))
                .value(Product::getName, equalTo("some-product"))
                .value(Product::getType, equalTo(FOOD));
    }

    @Test
    void shouldGetProductById() {

        Product product = Product.builder().id("some-id-x").name("some-product-x").type(FOOD).build();

        Mono<Product> productMono = Mono.just(product);

        when(productService.getProductById("some-id-x")).thenReturn(productMono);

        webTestClient.get()
                .uri("/api/product/{id}", "some-id-x")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class)
                .value(Product::getId, equalTo("some-id-x"))
                .value(Product::getName, equalTo("some-product-x"))
                .value(Product::getType, equalTo(FOOD));
    }

    @Test
    void shouldReturn404IfProductNotFoundById() {

        when(productService.getProductById("some-id-x")).thenThrow(new ProductNotFoundException(""));

        webTestClient.get()
                .uri("/api/product/{id}", "some-id-x")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }


}
