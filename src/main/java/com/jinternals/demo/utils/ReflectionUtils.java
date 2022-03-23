package com.jinternals.demo.utils;

import com.jinternals.demo.events.annotation.Event;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.core.env.Environment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.springframework.util.ReflectionUtils.doWithFields;

@Slf4j
public class ReflectionUtils {

    private ReflectionUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Map<Class<?>, String> getEventDestination(String eventPackage, Environment environment) {

        Reflections reflections = new Reflections(eventPackage);
        Set<Class<?>> eventClasses = reflections.getTypesAnnotatedWith(Event.class);

        return eventClasses.
                stream()
                .collect(toMap(identity(), cls -> getEventDestination(cls, environment)));

    }

    public static String getEventDestination(Class<?> cls, Environment environment) {
        String destination = environment.resolvePlaceholders(cls.getAnnotation(Event.class).destination());
        log.info("Event {} destination {}" , cls.getName(), destination);
        return destination;
    }

    public static List<Field> getFieldsAnnotatedWith(Class type, Class annotation) {
        List<Field> fields = new ArrayList<>();

        doWithFields(type, field -> {
            if (field.isAnnotationPresent(annotation)) {
                field.setAccessible(true);
                fields.add(field);
            }
        });

        return fields;
    }
}
