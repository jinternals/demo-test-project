package com.jinternals.demo.services;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class IdGeneratorTest {

    private String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    private IdGenerator idGenerator = new IdGenerator();

    @Test
     void shouldGenerateValidUUID(){
        String generatedId = idGenerator.generateId();

        assertThat(generatedId).hasSize(36);
        assertThat(generatedId).matches(UUID_REGEX);
    }

    @Test
     void shouldNotGenerateEmptyId(){
        String generatedId = idGenerator.generateId();

        assertThat(generatedId).isNotEmpty();
    }


    @Test
    void testMockStaticMethods() {

        assertThat(UUID.randomUUID()).isNotNull();

        final UUID someUUID = Mockito.mock(UUID.class);
        when(someUUID.toString()).thenReturn("some id");

        try (MockedStatic<UUID> uuid = Mockito.mockStatic(UUID.class)) {
            uuid.when(UUID::randomUUID).thenReturn(someUUID);
            assertThat(idGenerator.generateId()).isEqualTo("some id");
        }

        assertThat(UUID.randomUUID()).isNotNull();

    }




}
