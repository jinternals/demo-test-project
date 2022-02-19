package com.jinternals.demo.utils;

import com.jinternals.demo.domain.ProductType;
import com.jinternals.demo.events.annotation.Event;
import com.jinternals.demo.events.annotation.EventKey;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static com.jinternals.demo.utils.ReflectionUtils.getFieldsAnnotatedWith;
import static org.assertj.core.api.Assertions.assertThat;


class ReflectionUtilsTest {

    @Test
    void shouldReturnDestinationMap() {
        Map<Class<?>, String> eventDestination = ReflectionUtils.getEventDestination("com.jinternals.demo.utils");

        assertThat(eventDestination).hasSize(2);
        assertThat(eventDestination).containsEntry(Demo1Event.class, "demo_1");
        assertThat(eventDestination).containsEntry(Demo2Event.class, "demo_2");
    }


    @Test
    void shouldReturnKeyFiled() throws Exception {
        List<Field> demo1Fields = getFieldsAnnotatedWith(Demo1Event.class, EventKey.class);
        List<Field> demo2Fields = getFieldsAnnotatedWith(Demo2Event.class, EventKey.class);

        Field demo1FiledId = Demo1Event.class.getDeclaredField("id");
        Field demo2FiledId = Demo2Event.class.getDeclaredField("id");
        Field demo2FiledType = Demo2Event.class.getDeclaredField("type");


        assertThat(demo1Fields).contains(demo1FiledId);
        assertThat(demo2Fields).contains(demo2FiledId);
        assertThat(demo2Fields).contains(demo2FiledType);

        assertThat(demo1Fields).allMatch(field -> field.isAccessible());
        assertThat(demo2Fields).allMatch(field -> field.isAccessible());
    }



    @Data
    @Event(destination = "demo_1")
    @EqualsAndHashCode
    public class Demo1Event {
        @EventKey
        private String id;
        private String name;
        private String description;
        private ProductType type;
    }

    @Data
    @Event(destination = "demo_2")
    @EqualsAndHashCode
    public class Demo2Event {
        @EventKey(order = 0)
        private String id;
        private String name;
        private String description;
        @EventKey(order = 2)
        private ProductType type;
    }
}
