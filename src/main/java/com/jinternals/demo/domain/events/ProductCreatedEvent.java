package com.jinternals.demo.domain.events;

import com.jinternals.demo.domain.ProductType;
import com.jinternals.demo.events.annotation.Event;
import com.jinternals.demo.events.annotation.EventKey;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@Event(destination = "product")
@EqualsAndHashCode
public class ProductCreatedEvent {
    @EventKey
    private String id;
    private String name;
    private String description;
    private ProductType type;
}
