package com.jinternals.demo.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.index.CompositeQueryIndex;
import org.springframework.data.couchbase.core.index.QueryIndexed;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.stereotype.Indexed;

@Builder
@Data
@Document
@Indexed
//Keyspace index
@CompositeQueryIndex(
        fields = {
                "_class"
        })
@EqualsAndHashCode(of = {"id"})
@ToString
public class Product {
    @Id
    private String id;
    @Field
    private String name;
    @Field
    private String description;
    @Field
    @QueryIndexed
    private ProductType type;
}
