package com.jinternals.demo.event;

import com.jinternals.demo.exceptions.InvalidEventException;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;

import java.util.Map;
import java.util.Objects;

public class KafkaEventGateway implements EventGateway {

    private ReactiveKafkaProducerTemplate<String, Object> reactiveKafkaProducerTemplate;
    private Map<Class<?>, String> destination;

    public KafkaEventGateway(ReactiveKafkaProducerTemplate<String, Object> reactiveKafkaProducerTemplate,
                             Map<Class<?>, String> destination) {
        this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
        this.destination = destination;
    }

    public Mono<SenderResult<Void>> publish(Object event){

        String topic = destination.get(event.getClass());
        if(Objects.isNull(topic)){
           return Mono.error(new InvalidEventException(String.format("Please ensure your event is annotated with @Event(destination = \"your-destination-topic\")")));
        }
        return reactiveKafkaProducerTemplate.send(topic, event);
    }
}
