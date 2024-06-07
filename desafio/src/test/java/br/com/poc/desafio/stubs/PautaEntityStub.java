package br.com.poc.desafio.stubs;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import java.time.LocalDateTime;
import java.util.Random;

import br.com.poc.desafio.domain.entity.PautaEntity;

public class PautaEntityStub {

    private static final Random random = new Random();

    public static PautaEntity.PautaEntityBuilder generate() {
        return PautaEntity.builder()
            .id(random.nextLong())
            .titulo(randomAlphabetic(10))
            .dataCriacao(LocalDateTime.now());
    }
}
