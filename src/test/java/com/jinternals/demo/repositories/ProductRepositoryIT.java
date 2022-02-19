package com.jinternals.demo.repositories;

import com.jinternals.demo.Application;
import com.jinternals.demo.domain.Product;
import com.jinternals.demo.domain.ProductType;
import com.jinternals.demo.testcontainers.spring.CouchbaseContextInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.util.Arrays;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(
        initializers = { CouchbaseContextInitializer.class },
        classes = {Application.class, }
)
@DirtiesContext
public class ProductRepositoryIT {

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    public void init(){
        repository.deleteAll().subscribe();
    }

    @Test
    public void testSaveProductInformation() {
        Product apple = Product.builder().id("some-id-1").name("Apple").type(ProductType.FOOD).build();
        Product banana = Product.builder().id("some-id-2").name("Banana").type(ProductType.FOOD).build();

        repository.saveAll(Arrays.asList(apple, banana)).subscribe();

        StepVerifier
                .create(repository.findProductByType(ProductType.FOOD))
                .expectNextMatches(product -> product.equals(Product.builder().id("some-id-1").name("Apple").type(ProductType.FOOD).build()))
                .expectNextMatches(product -> product.equals(Product.builder().id("some-id-2").name("Banana").type(ProductType.FOOD).build()))
                .verifyComplete();

    }


}
