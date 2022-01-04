package com.jinternals.demo.services;

import com.jinternals.demo.domain.ProductType;
import com.jinternals.demo.domain.Product;
import com.jinternals.demo.exceptions.ProductNotFoundException;
import com.jinternals.demo.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.lang.String.format;
import static reactor.core.publisher.Mono.error;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Mono<Product> createProduct(Product product){
        return productRepository.save(product);
    }

    public Flux<Product> findProductByType(ProductType productType){
        return productRepository.findProductByType(productType);
    }

    public Mono<Product> getProductById(String productId) {
        return productRepository.findById(productId)
                .switchIfEmpty(
                        error(new ProductNotFoundException(format("Product with id %s not found.", productId )))
                );
    }
}
