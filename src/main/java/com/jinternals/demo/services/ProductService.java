package com.jinternals.demo.services;

import com.jinternals.demo.domain.ProductType;
import com.jinternals.demo.domain.Product;
import com.jinternals.demo.domain.events.ProductCreatedEvent;
import com.jinternals.demo.event.EventGateway;
import com.jinternals.demo.exceptions.ProductNotFoundException;
import com.jinternals.demo.repositories.ProductRepository;
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
    public final EventGateway eventGateway;

    public ProductService(ProductRepository productRepository,
                          EventGateway eventGateway) {
        this.productRepository = productRepository;
        this.eventGateway = eventGateway;
    }

    public Mono<Product> saveProduct(@Valid Product product) {
        return productRepository.save(product)
                .doOnSuccess(product1 -> {
                eventGateway.publish(toEvent(product1))
                        .doOnSuccess(voidSenderResult -> log.info("Published event successfully."))
                        .subscribe();
            });
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

    private ProductCreatedEvent toEvent(Product product){
        return   ProductCreatedEvent.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .type(product.getType())
                .build();

    }

}
