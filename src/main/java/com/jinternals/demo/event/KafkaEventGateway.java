package com.jinternals.demo.event;

import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;

import java.util.Map;

public class KafkaEventGateway implements EventGateway {

    private ReactiveKafkaProducerTemplate<String, Object> reactiveKafkaProducerTemplate;
    private Map<Class<?>, String> destination;

    public KafkaEventGateway(ReactiveKafkaProducerTemplate<String, Object> reactiveKafkaProducerTemplate,
                             Map<Class<?>, String> destination) {
        this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
        this.destination = destination;
    }

    public Mono<SenderResult<Void>> publish(Object event){
        return reactiveKafkaProducerTemplate.send(destination.get(event.getClass()), event);
    }
}
