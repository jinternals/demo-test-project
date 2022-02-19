package com.jinternals.demo.events;

import com.jinternals.demo.events.annotation.EventKey;
import com.jinternals.demo.events.exceptions.InvalidEventException;
import com.jinternals.demo.utils.ReflectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;

import java.lang.reflect.Field;
import java.util.*;

import static java.util.Comparator.comparingInt;
import static java.util.Objects.isNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class KafkaEventGateway implements EventGateway {

    private static final Map<Class<?>, List<Field>> fieldCache = new HashMap<>();
    private ReactiveKafkaProducerTemplate<String, Object> reactiveKafkaProducerTemplate;
    private Map<Class<?>, String> destination;

    public KafkaEventGateway(ReactiveKafkaProducerTemplate<String, Object> reactiveKafkaProducerTemplate,
                             Map<Class<?>, String> destinationTopicMapping) {
        this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
        this.destination = destinationTopicMapping;
    }

    @Override
    public Mono<SenderResult<Void>> publish(Object event) {

        String topic = destination.get(event.getClass());
        if (isNull(topic)) {
            return Mono.error(
                    new InvalidEventException("Please ensure your event is annotated with @Event(destination = \"your-destination-topic\")")
            );
        }
        Optional<String> key = getKey(event);
        if (key.isPresent()) {
            return reactiveKafkaProducerTemplate.send(topic, key.get(), event);
        } else {
            return reactiveKafkaProducerTemplate.send(topic, event);
        }
    }

    @NotNull
    private Optional<String> getKey(Object event) {
        if (!fieldCache.containsKey(event.getClass())) {
            fieldCache.put(event.getClass(), getOrderedFields(event));
        }

        String key = buildKey(event);
        return StringUtils.hasLength(key) ? of(key) : empty();
    }

    @NotNull
    private String buildKey(Object event) {
        return fieldCache.get(event.getClass())
                .stream()
                .map(field -> org.springframework.util.ReflectionUtils.getField(field, event))
                .filter(Objects::nonNull)
                .map(o -> o.toString())
                .collect(joining("."));
    }

    @NotNull
    private List<Field> getOrderedFields(Object event) {
        List<Field> fields = ReflectionUtils.getFieldsAnnotatedWith(event.getClass(), EventKey.class);
        return fields
                .stream()
                .sorted(comparingInt(o -> o.getAnnotation(EventKey.class).order()))
                .collect(toList());
    }


}
