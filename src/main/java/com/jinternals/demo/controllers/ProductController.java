package com.jinternals.demo.controllers;

import com.jinternals.demo.controllers.requests.CreateProductRequest;
import com.jinternals.demo.domain.Product;
import com.jinternals.demo.domain.ProductType;
import com.jinternals.demo.services.ProductService;
import com.jinternals.demo.utils.IDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;
    private final IDGenerator idGenerator;

    public ProductController(ProductService productService,
                             IDGenerator idGenerator) {
        this.productService = productService;
        this.idGenerator = idGenerator;
    }

    @PostMapping(value = "/product", consumes = "application/json")
    @ResponseStatus(CREATED)
    public Mono<Product> saveProduct(@RequestBody @Valid CreateProductRequest productRequest) {
        Product product = fromRequest(productRequest);
        product.setId(idGenerator.generateId());
        return productService.saveProduct(product);
    }

    @GetMapping(value = "/product/{productId}", produces = "application/json")
    @ResponseStatus(OK)
    public Mono<Product> getProduct(@PathVariable("productId") String productId) {
        return productService.getProductById(productId);
    }

    private Product fromRequest(CreateProductRequest productRequest){
        return   Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .type(ProductType.valueOf(productRequest.getType()))
                .build();

    }
}
