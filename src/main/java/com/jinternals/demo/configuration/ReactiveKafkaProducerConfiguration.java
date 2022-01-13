package com.jinternals.demo.configuration;

import com.jinternals.demo.event.KafkaEventGateway;
import com.jinternals.demo.utils.ReflectionsUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderOptions;

import java.util.Map;

@Configuration
public class ReactiveKafkaProducerConfiguration {

    @Bean
    public ReactiveKafkaProducerTemplate<String, Object> reactiveKafkaProducerTemplate
            (KafkaProperties properties) {
        Map<String, Object> props = properties.buildProducerProperties();
        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(props));
    }

    @Bean
    public Map<Class<?>, String> destinations(@Value("${event.package}")
                                                   String eventPackage) {
        return ReflectionsUtils.getEventDestination(eventPackage);
    }

    @Bean
    public KafkaEventGateway kafkaEventGateway(
            ReactiveKafkaProducerTemplate<String, Object> reactiveKafkaProducerTemplate,
            Map<Class<?>, String> destinations) {
        return new KafkaEventGateway(reactiveKafkaProducerTemplate, destinations);
    }

}
