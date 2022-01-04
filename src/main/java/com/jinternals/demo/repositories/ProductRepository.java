package com.jinternals.demo.repositories;

import com.jinternals.demo.domain.ProductType;
import com.jinternals.demo.domain.Product;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, String> {

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND type = $1 ORDER BY name")
    Flux<Product> findProductByType(ProductType productType);


}
