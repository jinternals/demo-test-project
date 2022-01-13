package com.jinternals.demo.event;

import com.jinternals.demo.domain.Product;
import com.jinternals.demo.domain.ProductType;
import com.jinternals.demo.domain.events.ProductCreatedEvent;
import com.jinternals.demo.exceptions.InvalidEventException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderResult;

import javax.validation.ConstraintViolationException;
import java.util.Map;

import static com.jinternals.demo.utils.ValidationTestUtil.validationProxy;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static reactor.core.publisher.Mono.just;

@ExtendWith(MockitoExtension.class)
class KafkaEventGatewayTest {

    @Mock
    private ReactiveKafkaProducerTemplate<String, Object> reactiveKafkaProducerTemplate;
    @Mock
    private Map<Class<?>, String> destination;

    private EventGateway kafkaEventGateway;

    @BeforeEach
    public void initializeTest() {
        kafkaEventGateway = validationProxy(new KafkaEventGateway(reactiveKafkaProducerTemplate, destination));
    }

    @Test
    public void shouldVerifyTemplatePublishEvent(){
        ProductCreatedEvent productCreatedEvent = ProductCreatedEvent.builder()
                .id("some-id")
                .name("some-name")
                .description("some-description")
                .type(ProductType.FOOD)
                .build();

        when(reactiveKafkaProducerTemplate.send("product", productCreatedEvent))
                .thenReturn(just(mock(SenderResult.class)));

        when(destination.get(ProductCreatedEvent.class))
                .thenReturn("product");

        kafkaEventGateway.publish(productCreatedEvent).subscribe();

        verify(reactiveKafkaProducerTemplate).send("product", productCreatedEvent);
        verify(destination).get(ProductCreatedEvent.class);
    }

    @Test
    public void shouldThrowInvalidEventException() {

        Product product = Product.builder()
                .id("some-id")
                .name("some-name")
                .description("some-description")
                .type(ProductType.FOOD)
                .build();

        when(destination.get(Product.class))
                .thenReturn(null);

        assertThatThrownBy(() -> kafkaEventGateway.publish(product).block())
                .isInstanceOf(InvalidEventException.class)
                .hasMessageContaining("Please ensure your event is annotated with @Event(destination = \"your-destination-topic\")");

        verify(destination).get(Product.class);

    }

    @Test
    public void shouldThrowConstraintViolationException() {
        ProductCreatedEvent productCreatedEvent = ProductCreatedEvent.builder()
                .build();

        assertThatThrownBy(() -> kafkaEventGateway.publish(productCreatedEvent))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Empty or null id is not allowed")
                .hasMessageContaining("Null productType is not allowed")
                .hasMessageContaining("Empty or null name is not allowed");

    }

}
