package com.jinternals.demo.domain.events;


import com.jinternals.demo.domain.ProductType;
import com.jinternals.demo.event.annotation.Event;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@Event(destination = "product")
@EqualsAndHashCode
public class ProductCreatedEvent {
    private String id;
    private String name;
    private String description;
    private ProductType type;
}
