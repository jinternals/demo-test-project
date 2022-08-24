package com.jinternals.demo.services;

import com.jinternals.demo.domain.Product;
import com.jinternals.demo.domain.ProductType;
import com.jinternals.demo.domain.events.ProductCreatedEvent;
import com.jinternals.demo.events.EventGateway;
import com.jinternals.demo.exceptions.ProductNotFoundException;
import com.jinternals.demo.repositories.ProductRepository;
import com.jinternals.demo.validators.group.OnCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static java.lang.String.format;
import static reactor.core.publisher.Mono.error;

@Slf4j
@Service
@Validated
public class ProductService {

    private final ProductRepository productRepository;
    private final EventGateway eventGateway;
    private final IdGenerator idGenerator;

    public ProductService(ProductRepository productRepository, EventGateway eventGateway, IdGenerator idGenerator) {
        this.productRepository = productRepository;
        this.eventGateway = eventGateway;
        this.idGenerator = idGenerator;
    }


    @Validated(OnCreate.class)
    public Mono<Product> createProduct(@Valid Product product) {
        product.setId(idGenerator.generateId());
        return productRepository.save(product)
                .doOnSuccess(prod -> {
                    eventGateway.publish(toEvent(prod))
                            .doOnSuccess(senderResult -> log.info("Published ProductCreatedEvent"))
                            .subscribe();
                });

    }

    private ProductCreatedEvent toEvent(Product product) {
        return ProductCreatedEvent.builder()
                .id(product.getId())
                .type(product.getType())
                .name(product.getName())
                .description(product.getDescription())
                .build();
    }

    public Flux<Product> findProductByType(
            @NotNull(message = "productType should not be null")
                    ProductType productType) {

        return productRepository.findProductByType(productType);

    }

    public Mono<Product> getProductById(
            @NotEmpty(message = "productId should not be null or empty")
                    String productId) {

        return productRepository.findById(productId)
                .switchIfEmpty(
                        error(new ProductNotFoundException(format("Product with id %s not found.", productId)))
                );

    }
}
