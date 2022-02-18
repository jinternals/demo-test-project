package com.jinternals.demo.controllers;

import com.jinternals.demo.controllers.requests.CreateProductRequest;
import com.jinternals.demo.domain.Product;
import com.jinternals.demo.domain.ProductType;
import com.jinternals.demo.services.ProductService;
import com.jinternals.demo.services.IDGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

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
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> saveProduct(@RequestBody @Valid CreateProductRequest productRequest) {
        Product product = fromRequest(productRequest);
        product.setId(idGenerator.generateId());
        return productService.createProduct(product);
    }

    @GetMapping(value = "/product/{productId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
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
