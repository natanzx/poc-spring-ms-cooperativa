package br.com.poc.desafio.controller;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.poc.desafio.domain.request.PautaRequest;
import br.com.poc.desafio.domain.request.SessaoVotacaoRequest;
import br.com.poc.desafio.domain.request.VotoAssociadoRequest;
import br.com.poc.desafio.domain.type.VotoType;
import br.com.poc.desafio.service.CadastrarPautaService;
import br.com.poc.desafio.service.CadastrarSessaoVotacaoService;
import br.com.poc.desafio.service.CadastrarVotoAssociadoService;
import br.com.poc.desafio.service.ConsolidarResultadoPautaService;
import br.com.poc.desafio.service.ListarPautasService;
import lombok.SneakyThrows;

@ExtendWith(MockitoExtension.class)
class PautaControllerTest {

    public MockMvc mockMvc;

    public final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private PautaController controller;

    @Mock
    private CadastrarPautaService cadastrarPautaService;

    @Mock
    private CadastrarSessaoVotacaoService cadastrarSessaoVotacaoService;

    @Mock
    private CadastrarVotoAssociadoService cadastrarVotoAssociadoService;

    @Mock
    private ConsolidarResultadoPautaService consolidarResultadoPautaService;

    @Mock
    private ListarPautasService listarPautasService;

    private final Random random = new Random();

    @BeforeEach
    void init() {
        mockMvc = standaloneSetup(controller).build();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @SneakyThrows
    @Test
    void deveCadastrarPautaComSucesso() {
        final var request = new PautaRequest(randomAlphabetic(10));

        mockMvc.perform(post("/v1/pautas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(cadastrarPautaService).cadastrar(request);
    }

    @SneakyThrows
    @Test
    void deveListarPautasComSucesso() {
        mockMvc.perform(get("/v1/pautas")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());

        verify(listarPautasService).listar();
    }

    @SneakyThrows
    @Test
    void deveCadastrarSessaoVotacaoComSucesso() {
        final var pautaId = random.nextLong();
        final var request = new SessaoVotacaoRequest(random.nextInt(1, 50));

        mockMvc.perform(post("/v1/pautas/{pautaId}/sessoes", pautaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(cadastrarSessaoVotacaoService).cadastrar(pautaId, request);
    }

    @SneakyThrows
    @Test
    void deveCadastrarVotoAssociadoComSucesso() {
        final var pautaId = random.nextLong();
        final var request = new VotoAssociadoRequest(RandomStringUtils.randomNumeric(11), VotoType.SIM);

        mockMvc.perform(post("/v1/pautas/{pautaId}/votos", pautaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(cadastrarVotoAssociadoService).cadastrar(pautaId, request);
    }

    @SneakyThrows
    @Test
    void deveConsolidarPautaComSucesso() {
        final var pautaId = random.nextLong();

        mockMvc.perform(get("/v1/pautas/{pautaId}/resultados", pautaId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());

        verify(consolidarResultadoPautaService).consolidar(pautaId);
    }
}