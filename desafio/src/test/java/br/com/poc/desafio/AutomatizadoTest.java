package br.com.poc.desafio;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.poc.desafio.domain.request.PautaRequest;
import br.com.poc.desafio.domain.response.PautaResponse;
import lombok.SneakyThrows;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AutomatizadoTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    public final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @SneakyThrows
    @Test
    void deveListarNenhumaPauta() {
        final String url = "http://localhost:" + port + "/api/v1/pautas";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        final var pautas = Arrays.asList(objectMapper.readValue(response.getBody(), PautaResponse[].class));

        assertEquals(0, pautas.size());
    }

    @SneakyThrows
    @Test
    void deveCadastrarPautaEListar() {
        final String url = "http://localhost:" + port + "/api/v1/pautas";

        final var request = new PautaRequest("Pauta de teste");

        final ResponseEntity<String> cadastrarPautaResponse = restTemplate.postForEntity(url, request, String.class);
        assertEquals(HttpStatus.CREATED, cadastrarPautaResponse.getStatusCode());

        Thread.sleep(1000);

        final ResponseEntity<String> listarPautaResponse = restTemplate.getForEntity(url, String.class);
        assertEquals(HttpStatus.OK, listarPautaResponse.getStatusCode());

        final var pautas = Arrays.asList(objectMapper.readValue(listarPautaResponse.getBody(), PautaResponse[].class));

        assertEquals(1, pautas.size());
        assertEquals(request.titulo(), pautas.get(0).titulo());
    }
}
