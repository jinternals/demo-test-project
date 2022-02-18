package com.jinternals.demo.utils;

import com.jinternals.demo.events.annotation.Event;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class ReflectionsUtils {

        private ReflectionsUtils() {
                throw new IllegalStateException("Utility class") ;
         }

    public static Map<Class<?>, String> getEventDestination(String eventPackage) {

        Reflections reflections = new Reflections (eventPackage);
        Set<Class<?>> eventClasses = reflections.getTypesAnnotatedWith(Event.class);

        return eventClasses.
                stream()
                .collect(toMap (identity() , cls -> cls.getAnnotation(Event.class).destination())) ;

    }
}
