package com.jinternals.demo.domain.events;


import com.jinternals.demo.domain.ProductType;
import com.jinternals.demo.event.annotation.Event;
import lombok.Data;

@Data
@Event(destination = "product")
public class ProductCreatedEvent {
    private String id;
    private String name;
    private String description;
    private ProductType type;
}
