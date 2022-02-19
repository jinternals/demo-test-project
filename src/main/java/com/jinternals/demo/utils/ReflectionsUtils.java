package com.jinternals.demo.utils;

import com.jinternals.demo.events.annotation.Event;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.springframework.util.ReflectionUtils.doWithFields;

public class ReflectionsUtils {

    private ReflectionsUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Map<Class<?>, String> getEventDestination(String eventPackage) {

        Reflections reflections = new Reflections(eventPackage);
        Set<Class<?>> eventClasses = reflections.getTypesAnnotatedWith(Event.class);

        return eventClasses.
                stream()
                .collect(toMap(identity(), cls -> cls.getAnnotation(Event.class).destination()));

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
