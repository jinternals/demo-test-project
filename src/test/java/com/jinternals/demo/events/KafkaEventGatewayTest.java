package com.jinternals.demo.events;


import com.jinternals.demo.domain.ProductType;
import com.jinternals.demo.events.annotation.Event;
import com.jinternals.demo.events.annotation.EventKey;
import com.jinternals.demo.events.exceptions.InvalidEventException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderResult;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static reactor.core.publisher.Mono.just;

@ExtendWith(MockitoExtension.class)
class KafkaEventGatewayTest {

    private KafkaEventGateway kafkaEventGateway;

    @Mock
    private ReactiveKafkaProducerTemplate<String, Object> template;

    @Mock
    private Map<Class<?>, String> destinations;

    @BeforeEach
    void setUp() {
        kafkaEventGateway = new KafkaEventGateway(template, destinations);
    }

    @Test
    void shouldPublishMessageWithKey() {
        Demo1Event demo1Event = new Demo1Event();
        demo1Event.setId("some-id");
        demo1Event.setType(ProductType.FOOD);
        when(destinations.get(Demo1Event.class)).thenReturn("demo_1");
        when(template.send("demo_1", "some-id.FOOD", demo1Event)).thenReturn(just(mock(SenderResult.class)));


        kafkaEventGateway.publish(demo1Event).subscribe();


        verify(template).send("demo_1", "some-id.FOOD", demo1Event);
        verify(destinations).get(Demo1Event.class);
    }

    @Test
    void shouldPublishMessageWithoutKey() {
        Demo2Event demo2Event = new Demo2Event();
        demo2Event.setId("some-id");
        demo2Event.setType(ProductType.FOOD);
        when(destinations.get(Demo2Event.class)).thenReturn("demo_2");
        when(template.send("demo_2", demo2Event)).thenReturn(just(mock(SenderResult.class)));


        kafkaEventGateway.publish(demo2Event).subscribe();


        verify(template).send("demo_2", demo2Event);
        verify(destinations).get(Demo2Event.class);
    }

    @Test
    void shouldThrowInvalidEventException() {
        when(destinations.get(String.class)).thenReturn(null);

        assertThatThrownBy(() ->
                kafkaEventGateway.publish("demo2Event").block()
        ).isInstanceOf(InvalidEventException.class)
                .hasMessageContaining("Please ensure your event is annotated with @Event(destination = \"your-destination-topic\")");

    }


    @Data
    @Event(destination = "demo_1")
    @EqualsAndHashCode
    public class Demo1Event {
        @EventKey(order = 0)
        private String id;
        private String name;
        private String description;
        @EventKey(order = 1)
        private ProductType type;
    }

    @Data
    @Event(destination = "demo_2")
    @EqualsAndHashCode
    public class Demo2Event {
        private String id;
        private String name;
        private String description;
        private ProductType type;
    }
}
