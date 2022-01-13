package com.jinternals.demo.domain.events;


import com.jinternals.demo.domain.ProductType;
import com.jinternals.demo.event.annotation.Event;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.index.QueryIndexed;
import org.springframework.data.couchbase.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@EqualsAndHashCode
@Event(destination = "product")
public class ProductCreatedEvent {
    @Id
    @NotEmpty(message = "Empty or null id is not allowed")
    private String id;
    @Field
    @NotEmpty(message = "Empty or null name is not allowed")
    private String name;
    @Field
    private String description;
    @Field
    @QueryIndexed
    @NotNull(message = "Null productType is not allowed")
    private ProductType type;
}
