package com.jinternals.demo.events;

import com.jinternals.demo.events.exceptions.InvalidEventException;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;

import java.util.Map;

import static java.util.Objects.isNull;

public class KafkaEventGateway implements EventGateway {

    private ReactiveKafkaProducerTemplate<String, Object> reactiveKafkaProducerTemplate;
    private Map<Class<?>, String> destination;

    public KafkaEventGateway(ReactiveKafkaProducerTemplate<String, Object> reactiveKafkaProducerTemplate,
                             Map<Class<?>, String> destination) {
        this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
        this.destination = destination;
    }

    @Override
    public Mono<SenderResult<Void>> publish(Object event) {

        String topic = destination.get(event.getClass());
        if (isNull(topic)) {
            return Mono.error(
                    new InvalidEventException("Please ensure your event is annotated with @Event(destination = \"your-destination-topic\")")
            );
        }
        return reactiveKafkaProducerTemplate.send(topic, event);
    }
}
