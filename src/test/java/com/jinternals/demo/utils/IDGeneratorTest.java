package com.jinternals.demo.utils;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

class IDGeneratorTest {

    private String UUID_REGEX = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$";
    private IDGenerator idGenerator = new IDGenerator();

    @Test
    public void shouldGenerateValidUUID(){
        String generatedId = idGenerator.generateId();

        assertThat(generatedId).hasSize(36);
        assertThat(generatedId).matches(UUID_REGEX);
    }

    @Test
    public void shouldNotGenerateEmptyId(){
        String generatedId = idGenerator.generateId();

        assertThat(generatedId).isNotEmpty();
    }


    @Test
    public void shouldNotGenerateBlankId(){
        String generatedId = idGenerator.generateId();

        assertThat(generatedId).isNotBlank();
    }

    @Test
    public void shouldAlwaysGenerateUniqueIds(){
        Random random = new Random();
        int randomNumber = random.nextInt(1000);

        Set<String> generatedIds = IntStream.range(1, 1 + randomNumber)
                .mapToObj(operand -> idGenerator.generateId())
                .collect(toSet());

        assertThat(generatedIds).hasSize(randomNumber);

    }






}
