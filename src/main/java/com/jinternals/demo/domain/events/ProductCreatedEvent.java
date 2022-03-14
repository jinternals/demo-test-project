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
    private String id;
    private String name;
    private String description;
    private ProductType type;
}
