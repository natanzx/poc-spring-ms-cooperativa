package br.com.poc.desafio.service;

import org.springframework.stereotype.Service;

import br.com.poc.desafio.client.UserFeign;
import br.com.poc.desafio.client.response.ValidarUserResponse.StatusVotacao;
import br.com.poc.desafio.domain.entity.VotoAssociadoEntity;
import br.com.poc.desafio.domain.request.VotoAssociadoRequest;
import br.com.poc.desafio.exception.core.BusinessException;
import br.com.poc.desafio.repository.PautaRepository;
import br.com.poc.desafio.repository.VotoAssociadoRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CadastrarVotoAssociadoService {

    private final VotoAssociadoRepository votoAssociadoRepository;
    private final BuscarSessaoVotacaoAbertaService buscarSessaoVotacaoAbertaService;
    private final ValidarAssociadoVotouEmPautaService validarAssociadoVotouEmPautaService;
    private final PautaRepository pautaRepository;
    private final UserFeign userFeign;

    public void cadastrar(final Long pautaId, final VotoAssociadoRequest request) {

        validarUsuarioValido(request);
        validarUsuarioJaVotou(pautaId, request);
        validarPautaExistente(pautaId);

        final var sessaoVotacaoEmAberto = buscarSessaoVotacaoAbertaService.buscar(pautaId)
            .orElseThrow(() -> new BusinessException("Nenhuma sessao de votacao em aberto"));

        final var entity = VotoAssociadoEntity.builder()
            .associadoCpf(request.associadoCpf())
            .voto(request.voto())
            .sessaoVotacao(sessaoVotacaoEmAberto)
            .build();

        votoAssociadoRepository.save(entity);
    }

    private void validarPautaExistente(final Long pautaId) {
        log.info("[VALIDACAO] Validando se a pauta existe");

        pautaRepository.findById(pautaId)
            .orElseThrow(() -> new BusinessException("Pauta não existente"));

        log.info("Pauta {} existente", pautaId);
    }

    private void validarUsuarioJaVotou(final Long pautaId, final VotoAssociadoRequest request) {
        log.info("[VALIDACAO] Validando se associado já votou.");

        final var validarAssociadoJaVotou = validarAssociadoVotouEmPautaService.validar(pautaId,
            request.associadoCpf());

        if (validarAssociadoJaVotou) {
            log.info("Associado {} já votou na pauta {}", request.associadoCpf(), pautaId);
            throw new BusinessException("Associado já votou nessa pauta");
        }

        log.info("Associado {} ainda não votou na pauta {}", request.associadoCpf(), pautaId);
    }

    private void validarUsuarioValido(final VotoAssociadoRequest request) {
        log.info("[VALIDACAO] Validando se o associado é valido...");

        try {
            final var status = userFeign.validarUser(request.associadoCpf()).getStatus();

            if (StatusVotacao.UNABLE_TO_VOTE.equals(status)) {
                log.error("Associado {} não permitido votar", request.associadoCpf());
                throw new BusinessException("Associado não permitido votar nessa pauta");
            }

            log.info("Associado {} permitido a votação", request.associadoCpf());
        } catch (FeignException.NotFound e) {
            log.error("CPF inválido, {}", request.associadoCpf());
            throw new BusinessException("CPF inválido");
        } catch (FeignException e) {
            throw new BusinessException("Falha ao validar associado");
        }
    }

}
