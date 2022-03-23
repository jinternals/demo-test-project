package com.jinternals.demo.configuration;

import com.jinternals.demo.events.KafkaEventGateway;
import com.jinternals.demo.utils.ReflectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderOptions;

import java.util.Map;

@Configuration
public class KafkaProducerConfiguration {

    @Bean
    public ReactiveKafkaProducerTemplate<String, Object> reactiveKafkaProducerTemplate
            (KafkaProperties kafkaProperties) {
        Map<String, Object> props = kafkaProperties.buildProducerProperties();
        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(props));
    }

    @Bean
    public Map<Class<?>, String> eventDestinations(@Value("${event.package}") String eventPackage, Environment environment) {
        return ReflectionUtils.getEventDestination(eventPackage, environment);
    }

    @Bean
    public KafkaEventGateway kafkaEventGateway(
            ReactiveKafkaProducerTemplate<String, Object> reactiveKafkaProducerTemplate,
            Map<Class<?>, String> eventDestinations) {
        return new KafkaEventGateway(reactiveKafkaProducerTemplate, eventDestinations);
    }

}
